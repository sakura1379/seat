package com.zlr.seat.common.limit;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.common.limit
 * @Description
 * @create 2022-11-01-下午4:05
 */
@Data
public class Limit {

    /**
     * redis 限流key
     */
    private String key;

    /**
     * 限流次数
     */
    private long max;

    /**
     * 限流时间
     */
    private long timeout;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 限流信息
     */
    private String message;
}

