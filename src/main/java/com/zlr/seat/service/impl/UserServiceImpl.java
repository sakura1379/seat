package com.zlr.seat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlr.seat.common.constant.AuthConst;
import com.zlr.seat.common.constant.UserConstant;
import com.zlr.seat.common.sercurity.RedisTokenStore;
import com.zlr.seat.common.sercurity.ServerSecurityContext;
import com.zlr.seat.controller.request.UpdateUserRequest;
import com.zlr.seat.controller.request.UserRegisterRequest;
import com.zlr.seat.dao.mapper.RolePermissionMapper;
import com.zlr.seat.dao.mapper.UserMapper;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.entity.pojo.User;
import com.zlr.seat.exception.GlobleException;
import com.zlr.seat.service.IUserService;
import com.zlr.seat.service.SmsCodeService;
import com.zlr.seat.utils.QiniuUtils;
import com.zlr.seat.vo.StudentUserDetails;
import com.zlr.seat.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service.impl
 * @Description
 * @create 2022-09-26-下午4:54
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private SmsCodeService smsCodeService;

    @Resource
    RolePermissionMapper rolePermissionMapper;

    @Resource
    private RedisTokenStore tokenStore;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private QiniuUtils qiniuUtils;

    /**
     * 邮箱绑定code的redis前缀
     */
    private final static String REDIS_MAIL_CODE_PREFIX = "mail:code:";

    /**
     * 更换手机号 手机号验证redis前缀
     */
    private final static String REDIS_MOBILE_VALIDATED_PREFIX = "mobile:validated:";

    /**
     * 根据用户id获取userVo
     *
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = "user", key = "#id")
    public UserVo selectUserVoById(Long id) {
        User user = getById(id);
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        List<String> permissionCode = new ArrayList<>();
        Integer roleId = user.getRoleId();
        permissionCode = rolePermissionMapper.selectPermissionCode(roleId);
        userVo.setPermissionCode(permissionCode);
        return userVo;
    }

    /**
     * 根据用户名或手机号
     * @param username
     * @param mobile
     * @return
     */
    @Override
    public UserVo selectUserVoByUsernameOtherwiseMobile(String username, String mobile) {
        User user = selectUserByUsernameOtherwiseMobile(username, mobile);
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        Integer roleId = user.getRoleId();
        List<String> permissionCode = new ArrayList<>();
        permissionCode = rolePermissionMapper.selectPermissionCode(roleId);
        userVo.setPermissionCode(permissionCode);
        return userVo;
    }

    /**
     * 更新用户信息
     *
     * @param request
     */
    @Override
    public void update(UpdateUserRequest request) {
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        if (!request.getId().equals(userDetail.getId())) {
            throw new GlobleException(ResultStatus.ACCESS_LIMIT_REACHED);
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        Long userId = request.getId();
        user.setId(userId);
        updateById(user);

        tokenStore.clearUserCacheById(userId);
    }

    /**
     * 改密码
     * @param oldPassword
     * @param newPassword
     */
    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(oldPassword, userDetail.getPassword());
        if (!matches) {
            throw new GlobleException(ResultStatus.OLD_PASSWORD_ERROR);
        }
        User user = new User();
        Long userId = userDetail.getId();
        user.setId(userId);
        String encodePassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodePassword);
        updateById(user);
        // 清空用户缓存
        tokenStore.clearUserCacheById(userId);
    }

    /**
     * 重置密码
     * @param mobile
     * @param code
     * @param password
     */
    @Override
    public void resetPassword(String mobile, String code, String password) {
        // 判断用户是否存在
        UserVo userVo = selectUserVoByUsernameOtherwiseMobile(null, mobile);
        if (userVo == null) {
            throw new GlobleException(ResultStatus.USER_NOT_EXIST);
        }
        // 验证码校验
        checkSmsCode(mobile, code);
        StudentUserDetails userDetails = new StudentUserDetails();
        BeanUtils.copyProperties(userVo, userDetails);
        User newUser = new User();
        Long userId = userDetails.getId();
        newUser.setId(userId);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(password);
        newUser.setPassword(encodePassword);
        updateById(newUser);

        // 清空用户缓存
        tokenStore.clearUserCacheById(userId);
    }

    /**
     * 更换手机号  验证手机号
     * @param mobile
     * @param code
     */
    @Override
    public void validateMobile(String mobile, String code) {
        checkSmsCode(mobile, code);
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        if (!userDetail.getMobilePhoneNumber().equals(mobile)) {
            throw new GlobleException(ResultStatus.MOBILE_NOT_MATCH);
        }
        // 经过原手机号验证标识
        stringRedisTemplate.opsForValue().set(REDIS_MOBILE_VALIDATED_PREFIX + mobile, mobile, 5L, TimeUnit.MINUTES);
        smsCodeService.deleteSmsCode(mobile);
    }

    /**
     * 更换手机号重新绑定
     * @param mobile
     * @param code
     */
    @Override
    public void rebindMobile(String mobile, String code) {
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        String cacheKey = userDetail.getMobilePhoneNumber();
        // 判断是否经过步骤一
        String validated = stringRedisTemplate.opsForValue().get(REDIS_MOBILE_VALIDATED_PREFIX + cacheKey);
        if (StringUtils.isBlank(validated)) {
            throw new GlobleException(ResultStatus.MOBILE_NOT_CHECK);
        }
        // 验证码校验
        checkSmsCode(mobile, code);
        // 判断手机号是否已被注册
        User user = selectUserByUsernameOrMobile(null, mobile);
        if (user != null) {
            throw new GlobleException(ResultStatus.MOBILE_EXIST);
        }
        User newUser = new User();
        Long userId = userDetail.getId();
        newUser.setId(userId);
        newUser.setMobilePhoneNumber(mobile);
        updateById(newUser);
        // 清空用户缓存
        tokenStore.clearUserCacheById(userId);
        smsCodeService.deleteSmsCode(mobile);
    }

    @Override
    public void status(long userId, Integer status) {
        // 状态，0：正常，1：锁定，2：禁用，3：过期
        int min = 0;
        int max = 3;
        if (status < min || status > max) {
            throw new GlobleException(ResultStatus.TYPE_ERROR);
        }
        User daoUser = getById(userId);
        if (daoUser == null) {
            throw new GlobleException(ResultStatus.USER_NOT_EXIST);
        }
        // 数据库数据更新
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        updateById(user);

        // 清空用户缓存
        tokenStore.clearUserCacheById(userId);
    }

    /**
     * 绑定手机号 - 用于原手机号为空的情况下
     * @param mobile
     * @param code
     */
    @Override
    public void bindMobile(String mobile, String code) {
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        boolean hasMobile  = userDetail.getMobilePhoneNumber() != null;
        if (hasMobile) {
            throw new GlobleException(ResultStatus.MOBILE_EXIST);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getMobilePhoneNumber,mobile);
        int count = count(queryWrapper);
        if (count != 0) {
            throw new GlobleException(ResultStatus.MOBILE_EXIST);
        }
        // 验证码校验
        checkSmsCode(mobile, code);
        User user = new User();
        Long id = userDetail.getId();
        user.setId(id);
        user.setMobilePhoneNumber(mobile);
        updateById(user);
        smsCodeService.deleteSmsCode(mobile);
        // 清空用户缓存
        tokenStore.clearUserCacheById(id);
    }

    /**
     * 更新头像
     * @param file
     */
    @Override
    public void updateAvatar(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
//        String contentType = file.getContentType();
//        String extension = filename.substring(filename.lastIndexOf(".") + 1);
//        String name = System.currentTimeMillis() + "." + extension;
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        // 上传头像
        boolean upload = qiniuUtils.upload(file, fileName);
//            String fullPath = storage.upload(file.getInputStream(), name, contentType);
        if (!upload){
            throw new GlobleException(ResultStatus.FILE_NOT_STORAGE);
        }
        String fullPath = QiniuUtils.url + fileName;
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        User user = new User();
        Long userId = userDetail.getId();
        user.setId(userId);
        user.setHead(fullPath);
        updateById(user);
        // 删除原头像文件
        qiniuUtils.delete(fileName);

        // 清空用户缓存
        tokenStore.clearUserCacheById(userId);
    }

    /**
     * username 不为空时使用username查询，否则使用mobile查询
     *
     * @param username
     * @param mobile
     * @return
     */
    private User selectUserByUsernameOtherwiseMobile(String username, String mobile) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.lambda().eq(User::getNickname, username);
        } else {
            queryWrapper.lambda().eq(User::getMobilePhoneNumber, mobile);
        }
        return getOne(queryWrapper, false);
    }

    /**
     * 用户注册
     *
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterRequest request) {
        String mobile = request.getMobile();
        String code = request.getCode();
        checkSmsCode(mobile, code);
        String username = request.getUsername();
        User userDao = selectUserByUsernameOrMobile(username, mobile);
        if (userDao != null && username.equals(userDao.getNickname())) {
            throw new GlobleException(ResultStatus.NAME_EXIST);
        }
        if (userDao != null && mobile.equals(userDao.getMobilePhoneNumber())) {
            throw new GlobleException(ResultStatus.MOBILE_EXIST);
        }
        User user = new User();
        user.setNickname(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        log.info(request.getPassword());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setMobilePhoneNumber(mobile);
        user.setRoleId(AuthConst.STUDENT);
//        String suffix = String.valueOf(mobile).substring(5);
//        user.setNickname("用户" + suffix);
//        user.setGender(UserConstant.GENDER_MALE);
//        user.setBirthday(LocalDate.now());
        user.setStatus(UserConstant.STATUS_NORMAL);
//        user.setCreateTime(LocalDateTime.now());
//        user.setAdmin(UserConstant.ORDINARY);
        user.setRegisterDate(new Date());
        user.setStatus(0);
        log.info(user.toString());
        save(user);
        smsCodeService.deleteSmsCode(mobile);
    }

    /**
     * 根据用户名或手机号查询 User
     *
     * @param username
     * @param mobile
     * @return
     */
    private User selectUserByUsernameOrMobile(String username, String mobile) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getNickname, username).or().eq(User::getMobilePhoneNumber, mobile);
        return getOne(queryWrapper, false);
    }


    /**
     * 校验短信验证码
     *
     * @param mobile
     * @param code
     */
    private void checkSmsCode(String mobile, String code) {
        if (!smsCodeService.checkSmsCode(mobile, code)) {
            throw new GlobleException(ResultStatus.SMS_WRONG);
        }
    }

    @Override
    public IPage<User> page(long current, long size, String nickname) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(nickname)) {
            queryWrapper.lambda().like(User::getNickname, nickname);
        }
        return page(new Page<>(current, size), queryWrapper);
    }


}
