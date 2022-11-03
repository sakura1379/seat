package com.zlr.seat.service.impl;

import com.zlr.seat.common.sercurity.MobileCodeAuthenticationToken;
import com.zlr.seat.common.sercurity.RedisTokenStore;
import com.zlr.seat.entity.pojo.Client;
import com.zlr.seat.service.AuthenticationService;
import com.zlr.seat.service.SmsCodeService;
import com.zlr.seat.vo.AuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service.impl
 * @Description
 * @create 2022-10-15-上午11:16
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisTokenStore tokenStore;

    @Resource
    private SmsCodeService smsCodeService;

    /**
     * 用户名或手机号密码认证
     * @param s  手机号或用户名
     * @param password 密码
     * @param client
     * @return
     */
    @Override
    public AuthenticationToken usernameOrMobilePasswordAuthenticate(String s, String password, Client client) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(s, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return tokenStore.storeToken(authenticate,client);
    }

    /**
     * 手机号验证码认证
     * @param mobile
     * @param code
     * @param client 客户端
     * @return
     */
    @Override
    public AuthenticationToken mobileCodeAuthenticate(String mobile, String code,Client client) {
        MobileCodeAuthenticationToken authenticationToken = new MobileCodeAuthenticationToken(mobile, code);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        AuthenticationToken storeAccessToken = tokenStore.storeToken(authenticate,client);
        smsCodeService.deleteSmsCode(mobile);
        return storeAccessToken;
    }

    /**
     * 移除 accessToken 相关
     * @param accessToken
     * @param client 客户端
     */
    @Override
    public void remove(String accessToken,Client client) {
        tokenStore.remove(accessToken,client);
    }

    /**
     * 刷新accessToken
     * @param refreshToken
     * @param client 客户端
     * @return
     */
    @Override
    public AuthenticationToken refreshAccessToken(String refreshToken,Client client) {
        return tokenStore.refreshAuthToken(refreshToken,client);
    }
}
