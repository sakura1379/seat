package com.zlr.seat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.vo
 * @Description 短信发送结果
 * @create 2022-10-13-下午2:58
 */
@Data
@AllArgsConstructor
public class SendResult {

    /**
     * 是否发送成功
     */
    private boolean success;

    /**
     * 发送的验证码
     */
    private String code;
}

