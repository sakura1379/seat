package com.zlr.seat.service.impl;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.zlr.seat.common.constant.SmsServiceProperties;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.exception.GlobleException;
import com.zlr.seat.vo.SendResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service.impl
 * @Description
 * @create 2022-10-13-下午5:32
 */
@Slf4j
public class TencentSmsCodeService extends BaseSmsCodeService{

    private String appId;
    private String appKey;
    private String templateId;
    private String signName;


    public TencentSmsCodeService (SmsServiceProperties properties) {

    }

    /**
     * 发送短信验证码实现
     *
     * @param mobile 手机号
     * @return
     */
    @Override
    protected SendResult handleSendSmsCode(String mobile) {
        SmsSingleSender sender = new SmsSingleSender(Integer.parseInt(appId), appKey);
        ArrayList<String> params = new ArrayList<>();
        String code = createCode();
        params.add(code);
        // 默认只能发送中国大陆的短信86
        try {
            SmsSingleSenderResult result = sender.sendWithParam("86", mobile, Integer.parseInt(templateId), params, signName, "", "");
            if (result.result != 0) {
                throw new GlobleException(ResultStatus.SMS_ERROR);
            }
            return new SendResult(true,code);
        } catch (Exception e) {
            log.error("发送短信失败:{0}", e);
            throw new GlobleException(ResultStatus.SMS_ERROR);
        }
    }
}
