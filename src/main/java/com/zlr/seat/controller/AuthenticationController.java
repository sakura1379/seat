package com.zlr.seat.controller;

import com.zlr.seat.controller.dto.AccessTokenDTO;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.entity.pojo.Client;
import com.zlr.seat.exception.GlobleException;
import com.zlr.seat.service.AuthenticationService;
import com.zlr.seat.service.IClientService;
import com.zlr.seat.service.impl.OauthService;
import com.zlr.seat.validator.annotation.IsPhone;
import com.zlr.seat.vo.AuthenticationToken;
import com.zlr.seat.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.controller
 * @Description
 * @create 2022-09-17-下午8:49
 */
@RestController
@Slf4j
@Api(tags = "认证服务", value = "/")
public class AuthenticationController {

    private static final String TOKEN_TYPE = "Bearer";

    private static final String AUTHORIZATION_TYPE = "Basic";

    @Resource
    private IClientService clientService;

    @Resource
    private OauthService oauthService;

    @Resource
    private AuthenticationService authenticationService;


    @PostMapping("/account/login")
    @ApiOperation(value = "账号密码登录", notes = "账号可以是用户名或手机号")
    public Result<AccessTokenDTO> accountLogin(@ApiParam("用户名或手机号") @NotBlank(message = "账号不能为空") @RequestParam String username,
                                                    @ApiParam("密码") @NotBlank(message = "密码不能为空") @RequestParam String password,
                                                    @ApiParam("客户端认证请求头") @RequestHeader(value = "Authorization") String authorization) {
        Client client = getAndValidatedClient(authorization);
        AuthenticationToken authenticationToken = authenticationService.usernameOrMobilePasswordAuthenticate(username, password, client);
        AccessTokenDTO response = new AccessTokenDTO();
        BeanUtils.copyProperties(authenticationToken, response);
        return Result.success(response);
    }

    @PostMapping("/mobile/login")
    @ApiOperation(value = "手机号验证码登录", notes = "验证码调用发送验证码接口获取")
    public Result<AccessTokenDTO> mobileLogin(@ApiParam("手机号") @NotNull(message = "手机号不能为空") @IsPhone @RequestParam String mobile,
                                                   @ApiParam("手机号验证码") @NotBlank(message = "验证码不能为空") @RequestParam String code,
                                                   @ApiParam("客户端认证请求头") @RequestHeader(value = "Authorization") String authorization) {
        Client client = getAndValidatedClient(authorization);
        AuthenticationToken authenticationToken = authenticationService.mobileCodeAuthenticate(mobile, code, client);
        AccessTokenDTO response = new AccessTokenDTO();
        BeanUtils.copyProperties(authenticationToken, response);
        return Result.success(response);
    }

    @PostMapping("/oauth")
    @ApiOperation(value = "第三方登录", notes = "不需要accessToken")
    public Result<AccessTokenDTO> oauth(
            @ApiParam("认证类型") @NotNull(message = "认证类型不能为空") @RequestParam Integer type,
            @ApiParam("第三方授权码") @NotBlank(message = "授权码不能为空") @RequestParam String code,
            @ApiParam("客户端认证请求头") @RequestHeader(value = "Authorization") String authorization) {
        Client client = getAndValidatedClient(authorization);
        AuthenticationToken authenticationToken = oauthService.oauth(type, code, client);
        AccessTokenDTO response = new AccessTokenDTO();
        BeanUtils.copyProperties(authenticationToken, response);
        return Result.success(response);
    }

    @PostMapping("/refresh_access_token")
    @ApiOperation(value = "刷新accessToken")
    public Result<AccessTokenDTO> refreshAccessToken(
            @ApiParam("客户端认证请求头") @RequestHeader(value = "Authorization") String authorization,
            @ApiParam("refresh_token") @NotBlank(message = "refresh_token不能为空") @RequestParam("refresh_token") String refreshToken) {
        Client client = getAndValidatedClient(authorization);
        AuthenticationToken authenticationToken = authenticationService.refreshAccessToken(refreshToken, client);
        AccessTokenDTO response = new AccessTokenDTO();
        BeanUtils.copyProperties(authenticationToken, response);
        return Result.success(response);
    }

    @DeleteMapping("/logout")
    @ApiOperation(value = "用户登出")
    public Result logout(@RequestHeader(value = "Authorization") String authorization,
                              @ApiParam("access_token") @RequestParam("access_token") String accessToken) {
        Client client = getAndValidatedClient(authorization);
        authenticationService.remove(accessToken, client);
        return Result.success();
    }

    /**
     * 获取并校验client
     *
     * @param authorization
     * @return
     */
    private Client getAndValidatedClient(String authorization) {
        String[] clientIdAndClientSecret = extractClientIdAndClientSecret(authorization);
        String clientId = clientIdAndClientSecret[0];
        String clientSecret = clientIdAndClientSecret[1];
        Client client = clientService.getClientByClientId(clientId);
        if (client == null || !clientSecret.equals(client.getClientSecret())) {
            throw new GlobleException(ResultStatus.CLIENT_ERROR);
        }
        return client;
    }

    /**
     * 提取客户端id和客户端密码
     *
     * @param authorization
     * @return
     */
    private String[] extractClientIdAndClientSecret(String authorization) {
        if (!authorization.startsWith(AUTHORIZATION_TYPE)) {
            throw new GlobleException(ResultStatus.CLIENT_ERROR);
        }
        String base64Data = authorization.substring(6);
        try {
            String data = new String(Base64.decodeBase64(base64Data));
            String separator = ":";
            String[] split = data.split(separator);
            int length = split.length;
            int matched = 2;
            if (length != matched) {
                throw new GlobleException(ResultStatus.CLIENT_ERROR);
            }
            return split;
        } catch (Exception e) {
            throw new GlobleException(ResultStatus.CLIENT_ERROR);
        }
    }
}
