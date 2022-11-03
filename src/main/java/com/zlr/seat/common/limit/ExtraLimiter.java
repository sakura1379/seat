package com.zlr.seat.common.limit;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.common.limit
 * @Description 附加接口限流接口
 * @create 2022-11-01-下午4:06
 */
public interface ExtraLimiter {

    /**
     *  附加自定义限流
     * @param rateLimiter 限流注解
     * @param point aop 切面point
     * @return 限流对象列表
     */
    List<Limit> limit(RateLimiter rateLimiter, ProceedingJoinPoint point);
}
