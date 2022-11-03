package com.zlr.seat.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.controller.dto
 * @Description
 * @create 2022-09-30-下午8:35
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="AccessTokenDTO", description="AccessTokenDTO")
public class AccessTokenDTO {

    @JsonProperty("access_token")
    @ApiModelProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    @ApiModelProperty("token类型:Bearer")
    private String tokenType;

    @JsonProperty("refresh_token")
    @ApiModelProperty("refresh_token")
    private String refreshToken;
}
