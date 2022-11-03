package com.zlr.seat.common.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.common.constant
 * @Description
 * @create 2022-10-13-下午5:08
 */
@Data
@ConfigurationProperties(prefix = "sms",ignoreInvalidFields = true) //读取yml配置？
public class SmsServiceProperties  {

    private int type = 1;

    private long expire = 300L;

    private long dayMax = 10L;

    private final SmsServiceProperties.Ali ali = new SmsServiceProperties.Ali();

    private final SmsServiceProperties.Tencent tencent = new SmsServiceProperties.Tencent();

    public SmsServiceProperties() {
    }

    @Data
    public static class Ali {
        private String regionId = "cn-hangzhou";
        private String accessKeyId;
        private String accessKeySecret;
        private String signName;
        private String templateCode;
    }

    @Data
    public static class Tencent {
        private String appId;
        private String appKey;
        private String templateId;
        private String signName;
    }
}