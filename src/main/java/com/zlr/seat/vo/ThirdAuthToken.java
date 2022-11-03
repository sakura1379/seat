package com.zlr.seat.vo;

import lombok.Data;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.vo
 * @Description
 * @create 2022-09-17-下午3:35
 */
@Data
public class ThirdAuthToken {
    private String accessToken;
    private int expire;
    private String refreshToken;
    private String uid;
    private String openId;
    private String accessCode;
    private String unionId;
}
