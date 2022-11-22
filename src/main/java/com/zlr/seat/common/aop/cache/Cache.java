package com.zlr.seat.common.aop.cache;

import java.lang.annotation.*;

/**
 * @author Zenglr
 * @program: simple_blog
 * @packagename: com.zlr.blog.common.cache
 * @Description
 * @create 2022-08-09-下午8:57
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    long expire() default 1 * 60 * 1000;
    //缓存标识 key
    String name() default "";

}
