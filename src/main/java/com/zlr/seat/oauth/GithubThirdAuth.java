package com.zlr.seat.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlr.seat.common.constant.OauthConstant;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.entity.pojo.ThirdAuthUser;
import com.zlr.seat.exception.GlobleException;
import com.zlr.seat.utils.HttpClientUtil;
import com.zlr.seat.vo.ThirdAuthToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.oauth
 * @Description
 * @create 2022-09-17-下午3:39
 */
@Slf4j
@Service
public class GithubThirdAuth{


    @Value("${oauth.github.clientId}")
    public String clientId;

    @Value("${oauth.github.clientSecret}")
    public String clientSecret;

    /**
     * 获取第三方用户信息
     *
     * @param accessToken
     * @return
     */
    public ThirdAuthUser getUserInfoByToken(String accessToken) {
        Map<String, String> params = new HashMap<>(2);
        params.put("access_token",accessToken);
        String result = HttpClientUtil.doGet(OauthConstant.GITHUB_ACCESS_USER_URL, params);
        if (StringUtils.isBlank(result)) {
            throw new GlobleException(ResultStatus.THIRD_TOKEN_ERROR);
        }
        log.info("请求结果:" + result);
        JSONObject jsonObject = JSON.parseObject(result);
        ThirdAuthUser thirdAuthUser = new ThirdAuthUser();
        thirdAuthUser.setUuid(jsonObject.getString("id"));
        thirdAuthUser.setNickname(jsonObject.getString("name"));
        thirdAuthUser.setAvatar(jsonObject.getString("avatar_url"));
        return thirdAuthUser;
    }


    /**
     * 获取第三方用户信息
     *
     * @param code
     * @return
     */
    public ThirdAuthUser getUserInfoByCode(String code) {
        ThirdAuthToken thirdAuthToken = getAuthToken(code);
        return getUserInfoByToken(thirdAuthToken.getAccessToken());
    }

    /**
     * 获取第三方token信息
     *
     * @param code
     * @return
     */
    public   ThirdAuthToken getAuthToken(String code) {
        Map<String, String> params = new HashMap<>(4);
        params.put("client_id",clientId);
        params.put("client_secret",clientSecret);
        params.put("code",code);
        String result = HttpClientUtil.doGet(OauthConstant.GITHUB_ACCESS_TOKE_URL, params);
        if (StringUtils.isBlank(result)) {
            throw new GlobleException(ResultStatus.THIRD_TOKEN_ERROR);
        }
        ThirdAuthToken thirdAuthToken = new ThirdAuthToken();
        thirdAuthToken.setAccessToken(getAccessToken(result));
        return thirdAuthToken;
    }

    /**
     * 获取 access_token
     * @param result
     * @return
     */
    private String getAccessToken(String result) {
        // result.csv.csv
        // access_token=aa5a59cd212b2c0f3c1f285822b2085f52fe3850&scope=user%3Aemail&token_type=bearer
        try {
            return result.split("&")[0].split("=")[1];
        }catch (ArrayIndexOutOfBoundsException e) {
            log.error("获取access_token异常", e);
            throw new GlobleException(ResultStatus.THIRD_TOKEN_ERROR);
        }
    }
}
