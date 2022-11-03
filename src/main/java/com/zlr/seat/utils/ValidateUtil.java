package com.zlr.seat.utils;

import java.util.regex.Pattern;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.utils
 * @Description
 * @create 2022-10-13-下午10:52
 */
public class ValidateUtil {

    /**
     *  验证是否手机号
     * @param s
     * @return boolean
     */
    public static boolean validateMobile(Object s) {
        String str = String.valueOf(s);
        String regexp = "^([1][3,4,5,6,7,8,9]\\d{9})$";
        return Pattern.matches(regexp, str);
    }
}
