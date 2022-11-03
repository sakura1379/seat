package com.zlr.seat.common.aop;

import java.lang.annotation.*;

/**
 * @author Zenglr
 * @program: simple_blog
 * @packagename: com.zlr.blog.common.aop
 * @Description
 * @create 2022-08-07-上午10:47
 */
//Type 代表可以放在类上面 Method 代表可以放在方法上
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operator() default "";
}
