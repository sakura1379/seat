package com.zlr.seat.common.filter;

import com.alibaba.fastjson.JSON;
import com.zlr.seat.common.sercurity.RedisTokenStore;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.vo.AuthenticationToken;
import com.zlr.seat.vo.Result;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.common.filter
 * @Description
 * @create 2022-10-05-下午2:51
 */
@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisTokenStore tokenStore;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String TOKEN_TYPE = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (authorization != null && authorization.startsWith(TOKEN_TYPE)) {
            String accessToken = authorization.substring(7);
            if (!accessToken.isEmpty()) {
                AuthenticationToken cacheAuthenticationToken = tokenStore.readByAccessToken(accessToken);
                if (cacheAuthenticationToken == null) { //redis或mysql中没有认证信息
                    httpServletResponse.setCharacterEncoding("UTF-8");
                    httpServletResponse.setContentType("application/json; charset=utf-8");
                    httpServletResponse.getWriter().print(JSON.toJSON(createErrorResponse()));
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(cacheAuthenticationToken.getPrincipal(), null, cacheAuthenticationToken.getPrincipal().getAuthorities());
                authentication.setDetails(cacheAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication); //设置上下文
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Result createErrorResponse() {
        Result response = new Result();
        response.setCode(ResultStatus.CREDENTIALS_INVALID.getCode());
        response.setMessage(ResultStatus.CREDENTIALS_INVALID.getMessage());
        return response;
    }
}
