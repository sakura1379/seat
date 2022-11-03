package com.zlr.seat.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.exception
 * @Description 手机号验证码认证 验证码不正确异常
 * @create 2022-10-13-下午5:47
 */
public class BadMobileCodeException extends AuthenticationException {

    public BadMobileCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadMobileCodeException(String msg) {
        super(msg);
    }
}

