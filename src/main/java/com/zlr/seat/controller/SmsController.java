package com.zlr.seat.controller;

import com.zlr.seat.common.constant.RedisConstant;
import com.zlr.seat.common.limit.RateLimiter;
import com.zlr.seat.service.SmsCodeService;
import com.zlr.seat.validator.annotation.IsPhone;
import com.zlr.seat.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.controller
 * @Description
 * @create 2022-10-17-下午5:04
 */
@Slf4j
@RestController
@RequestMapping("/sms")
@Api(tags = "短信验证码服务",value = "/sms")
public class SmsController {
    @Resource
    private SmsCodeService smsCodeService;



    @PostMapping("/send")
//    @RateLimiter(name = RedisConstant.SMS_LIMIT_NAME,max = 1,key = "#mobile", timeout = 120L, extra = "smsLimiter")
    @ApiOperation(value = "发送短信验证码",notes = "验证码有效时5分钟;同一手机号每天只能发10次;同一ip每天只能发10次;同一手机号限流120s一次")
    public Result sendSmsCode(@ApiParam("手机号") @NotNull @IsPhone @RequestParam String mobile) {
        smsCodeService.sendSmsCode(mobile);
        return Result.success();
    }
}
