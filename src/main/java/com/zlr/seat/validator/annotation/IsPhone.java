package com.zlr.seat.validator.annotation;

import com.zlr.seat.validator.IsPhoneValidator;
import org.springframework.messaging.handler.annotation.Payload;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.validator.annotation
 * @Description
 * @create 2022-09-15-下午9:23
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsPhoneValidator.class)
public @interface IsPhone {
    boolean required() default true;
    String message() default "手机号格式不正确";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
