package com.zlr.seat.common.sercurity;

import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.exception.GlobleException;
import com.zlr.seat.vo.AuthenticationToken;
import com.zlr.seat.vo.StudentUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.common.sercurity
 * @Description 服务安全上下文
 * @create 2022-10-13-上午11:20
 */
public class ServerSecurityContext {
    /**
     * 获取当前用户信息
     *
     * @return
     */
    public static StudentUserDetails getUserDetail(boolean throwEx) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null && throwEx) {
            throw new GlobleException(ResultStatus.CREDENTIALS_INVALID);
        }
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal == null && throwEx) {
            throw new GlobleException(ResultStatus.CREDENTIALS_INVALID);
        }
        String noneUser = "anonymousUser";
        if (noneUser.equals(principal)) {
            return null;
        }
        return (StudentUserDetails) principal;
    }

    /**
     * 获取认证信息
     *
     * @return org.springframework.security.core.Authentication
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    /**
     * 获取AuthenticationToken
     *
     * @return AuthenticationToken
     */
    public static AuthenticationToken getAuthenticationToken(boolean throwEx) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null && throwEx) {
            throw new GlobleException(ResultStatus.CREDENTIALS_INVALID);
        }
        if (authentication == null) {
            return null;
        }
        Object details = authentication.getDetails();
        if (details == null && throwEx) {
            throw new GlobleException(ResultStatus.CREDENTIALS_INVALID);
        }
        return (AuthenticationToken) details;
    }

}

