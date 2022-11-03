package com.zlr.seat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlr.seat.controller.request.UpdateUserRequest;
import com.zlr.seat.controller.request.UserRegisterRequest;
import com.zlr.seat.entity.pojo.User;
import com.zlr.seat.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service
 * @Description
 * @create 2022-09-18-下午4:43
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户id获取用户
     *
     * @param id
     * @return
     */
    UserVo selectUserVoById(Long id);

    /**
     * 根据用户名或手机号查询用户信息
     *
     * @param username
     * @param mobile
     * @return cn.poile.blog.entity.User
     */
    UserVo selectUserVoByUsernameOtherwiseMobile(String username, String mobile);

    /**
     * 用户注册
     *
     * @param request
     */
    void register(UserRegisterRequest request);

    /**
     * 更新用户信息
     *
     * @param request
     */
    void update(UpdateUserRequest request);

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @return void
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 重置密码
     *
     * @param mobile
     * @param code
     * @param password
     * @return void
     */
    void resetPassword(String mobile, String code, String password);

    /**
     * 更换手机号  验证手机号
     *
     * @param mobile
     * @param code
     * @return void
     */
    void validateMobile(String mobile, String code);

    /**
     * 更换手机号 重新绑定
     *
     * @param mobile
     * @param code
     * @return void
     */
    void rebindMobile(String mobile, String code);

    /**
     * 修改用户状态
     *
     * @param userId
     * @param status
     */
    void status(long userId, Integer status);

    /**
     * 绑定手机号 - 用于原手机号为空的情况
     * @param mobile
     * @param code
     */
    void bindMobile(String mobile, String code);

    /**
     * 更新头像
     *
     * @param file
     * @return void
     */
    void updateAvatar(MultipartFile file);


    /**
     * 分页查询用户
     *
     * @param current
     * @param size
     * @param nickname
     * @return
     */
    IPage<User> page(long current, long size, String nickname);
}
