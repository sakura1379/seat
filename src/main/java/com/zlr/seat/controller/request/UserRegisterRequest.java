package com.zlr.seat.controller.request;

import com.zlr.seat.validator.annotation.IsPhone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.controller.request
 * @Description
 * @create 2022-10-15-下午3:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户注册请json",description = "用户注册")
public class UserRegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名只能字母开头，允许2-16字节，允许字母数字下划线")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{1,15}$", message = "用户名只能字母开头，允许2-16字节，允许字母数字下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6,message = "密码至少6位数")
    @ApiModelProperty("密码")
    private String password;

    @NotNull(message = "手机号不能为空")
    @IsPhone
    @ApiModelProperty("手机号")
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String code;
}

