package com.zlr.seat.validator.annotation;

import com.zlr.seat.validator.IsImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.validator.annotation
 * @Description
 * @create 2022-10-19-下午2:18
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsImageValidator.class)
public @interface IsImage {
    boolean required() default true;
    String message() default "文件格式不正确，只限bmp,gif,jpeg,jpeg,png,webp格式";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
