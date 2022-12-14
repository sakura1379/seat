package com.zlr.seat.validator;

import com.zlr.seat.validator.annotation.IsImage;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.validator
 * @Description
 * @create 2022-10-19-下午2:18
 */
public class IsImageValidator implements ConstraintValidator<IsImage, MultipartFile> {

    private boolean required;

    private static final String[] IMAGE_CONTENT_TYPE_ARRAY = {"image/bmp", "image/gif", "image/jpeg","image/png", "image/jpg", "image/webp"};



    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (required) {
            String contentType = value.getContentType();
            List<String> imageContentTypeList = Arrays.asList(IMAGE_CONTENT_TYPE_ARRAY);
            return imageContentTypeList.contains(contentType);
        }
        return true;
    }

    @Override
    public void initialize(IsImage constraintAnnotation) {
        required = constraintAnnotation.required();
    }
}

