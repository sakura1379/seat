package com.zlr.seat.service;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service
 * @Description
 * @create 2022-10-13-下午2:48
 */
public interface SmsCodeService {
    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    boolean sendSmsCode(String mobile);

    /**
     * 缓存短信验证码
     * @param mobile
     * @param code
     */
    void cacheSmsCode(String mobile,String code);

    /**
     * 校验短信验证码
     * @param mobile
     * @param code
     * @return
     */
    boolean checkSmsCode(String mobile,String code);

    /**
     * 删除验证码
     * @param mobile
     * @return
     */
    boolean deleteSmsCode(String mobile);
}
