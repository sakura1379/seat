package com.zlr.seat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.zlr.seat.common.constant.SmsServiceProperties;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.exception.GlobleException;
import com.zlr.seat.vo.SendResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service.impl
 * @Description
 * @create 2022-10-13-下午5:20
 */
@Slf4j
public class AliSmsCodeService extends BaseSmsCodeService {

    private String regionId;

    private String accessKeyId;

    private String accessKeySecret;

    private String signName;

    private String templateCode;

    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private static final String ACTION = "SendSms";

    private static final String VERSION = "2017-05-25";


    public AliSmsCodeService(SmsServiceProperties properties) {
        setExpire(properties.getExpire());
        SmsServiceProperties.Ali ali = properties.getAli();
        init(ali);
    }

    private void init(SmsServiceProperties.Ali ali) {
        this.regionId = ali.getRegionId();
        this.accessKeyId = ali.getAccessKeyId();
        this.accessKeySecret = ali.getAccessKeySecret();
        this.signName = ali.getSignName();
        this.templateCode = ali.getTemplateCode();
    }


    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */
    @Override
    protected SendResult handleSendSmsCode(String mobile) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(DOMAIN);
        request.setVersion(VERSION);
        request.setAction(ACTION);
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        String code = createCode();
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            return handleCommonResponse(client.getCommonResponse(request), code);
        } catch (ClientException e) {
            log.error("发送短信失败:{0}", e);
            throw new GlobleException(ResultStatus.SMS_ERROR);
        }

    }

    /**
     * 短信发送结果解析
     *
     * @param response
     * @param code
     * @return
     */
    private SendResult handleCommonResponse(CommonResponse response, String code) {
        int httpStatus = response.getHttpStatus();
        if (httpStatus != HttpStatus.OK.value()) {
            throw new GlobleException(ResultStatus.SMS_ERROR);
        }
        String data = response.getData();
        JSONObject jsonObject = JSON.parseObject(data);
        String resultCode = (String) jsonObject.get("Code");
        String successCode = "OK";
        if (!successCode.equals(resultCode)) {
            String resultMessage = (String) jsonObject.get("Message");
            throw new GlobleException(ResultStatus.SMS_ERROR);
        }
        return new SendResult(true,code);
    }
}
