package com.zlr.seat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.vo
 * @Description
 * @create 2022-09-17-下午11:04
 */
@Data
@Accessors(chain = true)
@ApiModel(value="AuthenticationToken", description="AuthenticationToken")
public class AuthenticationToken implements Serializable {

    @ApiModelProperty("accessToken")
    private String accessToken;

    @ApiModelProperty("token类型:Bearer")
    private String tokenType = "Bearer";

    @ApiModelProperty("refreshToken")
    private String refreshToken;

    @ApiModelProperty(hidden = true,value = "用户信息")
    private StudentUserDetails principal;

  /*  @ApiModelProperty(hidden = true,value = "客户端id")
    private String clientId;*/
}

