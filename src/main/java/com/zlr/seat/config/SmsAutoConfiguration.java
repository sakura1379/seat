package com.zlr.seat.config;

import com.zlr.seat.common.constant.SmsServiceProperties;
import com.zlr.seat.service.SmsCodeService;
import com.zlr.seat.service.impl.AliSmsCodeService;
import com.zlr.seat.service.impl.TencentSmsCodeService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.config
 * @Description
 * @create 2022-10-13-下午5:07
 */
@Configuration
@EnableConfigurationProperties({SmsServiceProperties.class})
public class SmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmsCodeService smsService(SmsServiceProperties properties) {
        int type = properties.getType();
        if (type == 1) {
            return new AliSmsCodeService(properties);
        }
        return new TencentSmsCodeService(properties);
    }
}
