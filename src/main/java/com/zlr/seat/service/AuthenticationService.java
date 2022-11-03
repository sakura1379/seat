package com.zlr.seat.service;

import com.zlr.seat.entity.pojo.Client;
import com.zlr.seat.vo.AuthenticationToken;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service
 * @Description
 * @create 2022-10-15-上午11:00
 */
public interface AuthenticationService {

    /**
     *  用户名或手机号密码认证
     * @param s 手机号或用户名
     * @param password 密码
     * @param client 客户端
     * @return
     */
    AuthenticationToken usernameOrMobilePasswordAuthenticate(String s, String password,Client client);

    /**
     * 手机号验证码认证
     * @param mobile 手机号
     * @param code 验证码
     * @param client 客户端
     * @return
     */
    AuthenticationToken mobileCodeAuthenticate(String mobile,String code,Client client);

    /**
     * 移除 accessToken 相关
     * @param accessToken accessToken
     * @param client 客户端
     */
    void remove(String accessToken,Client client);

    /**
     * 刷新 accessToken
     * @param refreshToken refreshToken
     * @param client 客户端
     * @return
     */
    AuthenticationToken refreshAccessToken(String refreshToken, Client client);
}
