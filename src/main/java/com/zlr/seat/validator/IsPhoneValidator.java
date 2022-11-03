package com.zlr.seat.validator;

import com.zlr.seat.validator.annotation.IsPhone;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.validator.annotation
 * @Description
 * @create 2022-09-15-下午10:17
 */
public class IsPhoneValidator implements ConstraintValidator<IsPhone, Object> {

    private boolean required;

    @Override
    public void initialize(IsPhone ca) {
        required = ca.required();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String mobile = "" + o;
        if (!"".equals(mobile) && required) {
            String regexp = "^[1][3,4,5,6,7,8,9]\\d{9}$";
            return Pattern.matches(regexp, mobile);
        }
        return true;
    }
}
