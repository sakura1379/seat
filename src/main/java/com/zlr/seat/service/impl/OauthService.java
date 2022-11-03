package com.zlr.seat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlr.seat.common.constant.AuthConst;
import com.zlr.seat.common.constant.OauthConstant;
import com.zlr.seat.common.constant.UserConstant;
import com.zlr.seat.common.sercurity.PrimaryKeyAuthenticationToken;
import com.zlr.seat.common.sercurity.RedisTokenStore;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.entity.pojo.Client;
import com.zlr.seat.entity.pojo.OauthUser;
import com.zlr.seat.entity.pojo.ThirdAuthUser;
import com.zlr.seat.entity.pojo.User;
import com.zlr.seat.exception.GlobleException;
import com.zlr.seat.oauth.GithubThirdAuth;
import com.zlr.seat.service.IOauthUserService;
import com.zlr.seat.service.IUserService;
import com.zlr.seat.vo.AuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service.impl
 * @Description
 * @create 2022-09-18-下午2:35
 */
@Slf4j
@Service
public class OauthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private GithubThirdAuth githubThirdAuth;

    @Resource
    private IUserService userService;

    @Resource
    private IOauthUserService oauthUserService;

    @Resource
    private RedisTokenStore tokenStore;

    /**
     * 第三方登录
     * @param type
     * @param code
     * @param client
     * @return
     */
    public AuthenticationToken oauth(int type, String code, Client client) {
        if (type == OauthConstant.OAUTH_TYPE_GITHUB) {
            return githubOauth(type,code,client);
        }
//        } else if (type == OauthConstant.OAUTH_TYPE_GITEE) {
//            return giteeOauth(type, code, client);
//        } else if (type == OauthConstant.OAUTH_TYPE_QQ) {
//            return qqOauth(type,code,client);
//        }
        throw new GlobleException(ResultStatus.TYPE_ERROR);
    }

    /**
     * github第三方登录
     * @param type
     * @param code
     * @param client
     * @return
     */
    private AuthenticationToken githubOauth(int type, String code, Client client) {
        ThirdAuthUser thirdAuthUser = githubThirdAuth.getUserInfoByCode(code);
        String uuid = thirdAuthUser.getUuid();
        OauthUser oauthUser = checkBind(type, uuid);
        // 已绑定
        if (oauthUser != null) {
            log.info("bind already");
            PrimaryKeyAuthenticationToken authenticationToken = new PrimaryKeyAuthenticationToken(oauthUser.getUserId());
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            return tokenStore.storeToken(authenticate,client);
        }
        // 未绑定
        return bind(type,thirdAuthUser,client);
    }

    /**
     * 查询是否已绑定
     * @param type
     * @param uuid
     * @return
     */
    private OauthUser checkBind(int type, String uuid) {
        log.info("check bind");
        QueryWrapper<OauthUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OauthUser::getUuid,uuid).eq(OauthUser::getType,type);
        return oauthUserService.getOne(queryWrapper, false);
    }

    /**
     * 绑定
     * @param type
     * @param thirdAuthUser
     * @param client
     * @return
     */
    private AuthenticationToken bind(int type, ThirdAuthUser thirdAuthUser, Client client) {
        User user = new User();
        if(thirdAuthUser.getNickname() == null){
            user.setNickname("default-name"+Math.random()*100000);
        }else{
            user.setNickname(thirdAuthUser.getNickname());
        }
        user.setHead(thirdAuthUser.getAvatar());
        user.setStatus(UserConstant.STATUS_NORMAL);
        user.setRoleId(AuthConst.STUDENT);
        userService.save(user);
        // 关联表
        OauthUser newOauthUser = new OauthUser();
        newOauthUser.setUuid(thirdAuthUser.getUuid());
        newOauthUser.setUserId(user.getId());
        newOauthUser.setType(type);
        newOauthUser.setCreateTime(new Date());// new Date()为获取当前系统时间
        oauthUserService.save(newOauthUser);
        PrimaryKeyAuthenticationToken authenticationToken = new PrimaryKeyAuthenticationToken(user.getId());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return tokenStore.storeToken(authenticate,client);
    }
}
