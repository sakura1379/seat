package com.zlr.seat.config;

import com.alibaba.fastjson.JSON;
import com.zlr.seat.common.filter.AuthorizationTokenFilter;
import com.zlr.seat.common.sercurity.MobileCodeAuthenticationProvider;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.service.IUserService;
import com.zlr.seat.service.SmsCodeService;
import com.zlr.seat.vo.Result;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.config
 * @Description
 * @create 2022-10-05-下午1:51
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private SecurityIgnoreProperties ignoreProperties;

    @Resource
    private AuthorizationTokenFilter authorizationTokenFilter;

    @Resource
    private IUserService userService;

    @Resource
    private SmsCodeService smsCodeService;

    @Resource
    private UserDetailsService userDetailsService;

    /**
     * 配置认证管理器
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 这里是对认证管理器的添加配置
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider())
                .authenticationProvider(provider2())
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/v2/api-docs/**",
                        "/swagger-resources/configuration/ui", // 用来获取支持的动作
                        "/swagger-resources", // 用来获取api-docs的URI
                        "/swagger-resources/configuration/security", // 安全选项
                        "/webjars/**"
                );
    }
    private String[] loadExcludePath() {
        return new String[]{
                "*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/swagger-resources/configuration/ui", // 用来获取支持的动作
                "/swagger-resources", // 用来获取api-docs的URI
                "/swagger-resources/configuration/security", // 安全选项
                "/webjars/**"
        };
    }
    /**
     * 自定义主键查询用户认证提供者
     * @return
     */
    @Bean
    public PrimaryKeyAuthenticationProvider provider2() {
        PrimaryKeyAuthenticationProvider provider = new PrimaryKeyAuthenticationProvider();
        provider.setUserService(userService);
        return provider;
    }

    /**
     * 配置密码加密对象（解密时会用到PasswordEncoder的matches判断是否正确）
     * 用户的password用到，所以存的时候存该bean encode过的密码
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> ignorePropertiesList = ignoreProperties.getList();
        int size = ignorePropertiesList.size();
        http.cors()
                .and()
                .csrf().disable() // 使用jwt不需要csrf，关闭csrf保护功能（跨域访问）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers(ignorePropertiesList.toArray(new String[size])).permitAll()
                .antMatchers("/oauth","/refresh_access_token","/sms/send"
                        ,"/mobile/login","/user/register","/account/login","/password/reset","/seat/page").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));
        http.addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());

    }


    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (HttpServletRequest var1, HttpServletResponse var2, AuthenticationException var3) -> {
            if (var3 instanceof InsufficientAuthenticationException) {
                log.error(var3.toString());
                var2.setCharacterEncoding("UTF-8");
                var2.setContentType("application/json; charset=utf-8");
                Result response = new Result();
                response.setCode(ResultStatus.PERMISSION_DENIED.getCode());
                response.setMessage(ResultStatus.PERMISSION_DENIED.getMessage());
                var2.getWriter().print(JSON.toJSON(response));
            }
        };
    }

    /**
     * 自定义手机验证码认证提供者
     *
     * @return
     */
    @Bean
    public MobileCodeAuthenticationProvider provider() {
        MobileCodeAuthenticationProvider provider = new MobileCodeAuthenticationProvider();
        provider.setSmsCodeService(smsCodeService);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}
