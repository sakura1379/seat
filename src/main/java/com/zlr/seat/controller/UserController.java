package com.zlr.seat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zlr.seat.common.aop.LogAnnotation;
import com.zlr.seat.controller.request.UpdateUserRequest;
import com.zlr.seat.controller.request.UserRegisterRequest;
import com.zlr.seat.entity.pojo.User;
import com.zlr.seat.service.IUserService;
import com.zlr.seat.validator.annotation.IsImage;
import com.zlr.seat.validator.annotation.IsPhone;
import com.zlr.seat.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.controller
 * TODO 新增管理员、退出登录
 * @Description
 * @create 2022-10-15-下午3:17
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户服务", value = "/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/register")
    @LogAnnotation(module="登录服务",operator="用户注册")
    @ApiOperation(value = "用户注册", notes = "不需要传accessToken")
    public Result register(@Validated @RequestBody UserRegisterRequest request) {
        userService.register(request);
        return Result.success();
    }

    @GetMapping("/info")
    @LogAnnotation(module="登录服务",operator="获取用户信息")
    @ApiOperation(value = "获取用户信息", notes = "需要传accessToken")
    public Result<Object> info(Authentication authentication) {
        return Result.success(authentication.getPrincipal());
    }


    @PostMapping("/update")
    @LogAnnotation(module="登录服务",operator="更新用户基本信息")
    @ApiOperation(value = "更新用户基本信息", notes = "需要传accessToken，请求的json中id字段必传，更新不为null的项")
    public Result update(@Validated @RequestBody UpdateUserRequest request) {
        userService.update(request);
        return Result.success();
    }

    @PostMapping("/password/update")
    @LogAnnotation(module="登录服务",operator="修改密码")
    @ApiOperation(value = "修改密码", notes = "需要传accessToken,密码至少6位数")
    public Result updPassword(@ApiParam("原密码") @NotBlank(message = "旧密码不能为空") @RequestParam(value = "oldPassword") String oldPassword,
                                   @ApiParam("新密码") @NotBlank(message = "新密码不能为空") @Length(min = 6, message = "密码至少6位数") @RequestParam(value = "newPassword") String newPassword) {
        userService.updatePassword(oldPassword, newPassword);
        return Result.success();
    }

    @PostMapping("/password/reset")
    @LogAnnotation(module="登录服务",operator="重置密码")
    @ApiOperation(value = "重置密码", notes = "不需要传accessToken,需要验证手机号")
    public Result resetPassword(@ApiParam("手机号") @NotNull(message = "手机号不能为空") @IsPhone @RequestParam("mobile") String mobile,
                                     @ApiParam("验证码") @NotBlank(message = "验证码不能为空") @RequestParam("code") String code,
                                     @ApiParam("密码") @NotBlank(message = "密码不能为空") @Length(min = 6, message = "密码至少6位数") @RequestParam("password") String password) {
        userService.resetPassword(mobile, code, password);
        return Result.success();
    }

    @PostMapping("/avatar/update")
    @LogAnnotation(module="登录服务",operator="更新用户头像")
    @ApiOperation(value = "更新用户头像", notes = "文件只限bmp,gif,jpeg,jpeg,png,webp格式")
    public Result updAvatar(@ApiParam("头像图片文件") @IsImage @RequestPart(value = "file") MultipartFile file) {
        userService.updateAvatar(file);
        return Result.success();
    }

    @PostMapping("/mobile/validate")
    @LogAnnotation(module="登录服务",operator="更换手机号步骤一，验证原手机号")
    @ApiOperation(value = "更换手机号步骤一，验证原手机号", notes = "需要传accessToken")
    public Result validateMobile(@ApiParam("手机号") @NotNull(message = "手机号不能为空") @IsPhone @RequestParam("mobile") String mobile,
                                      @NotBlank(message = "验证码不能为空") @RequestParam("code") String code) {
        userService.validateMobile(mobile, code);
        return Result.success();
    }

    @PostMapping("/mobile/rebind")
    @LogAnnotation(module="登录服务",operator="更换手机号步骤二，绑定新手机号")
    @ApiOperation(value = "更换手机号步骤二，绑定新手机号", notes = "需要传accessToken")
    public Result rebindMobile(@ApiParam("手机号") @NotNull(message = "手机号不能为空") @IsPhone @RequestParam(value = "mobile") String mobile,
                                    @ApiParam("验证码") @NotBlank(message = "验证码不能为空") @RequestParam(value = "code") String code) {
        userService.rebindMobile(mobile, code);
        return Result.success();
    }

    @PostMapping("/mobile/bind")
    @LogAnnotation(module="登录服务",operator="绑定手机号")
    @ApiOperation(value = "绑定手机号", notes = "需要传accessToken，只用于原手机为空的情况下")
    public Result bindMobile(@ApiParam("手机号") @NotNull(message = "手机号不能为空") @IsPhone @RequestParam(value = "mobile") String mobile,
                                  @ApiParam("验证码") @NotBlank(message = "验证码不能为空") @RequestParam(value = "code") String code) {
        userService.bindMobile(mobile, code);
        return Result.success();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('console')")
    @LogAnnotation(module="登录服务",operator="分页获取用户信息，用于后台管理")
    @ApiOperation(value = "分页获取用户信息，用于后台管理", notes = "需要accessToken，需要管理员权限")
    public Result<IPage<User>> page(@ApiParam("页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
                                         @ApiParam("每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size,
                                         @ApiParam("昵称") @RequestParam(value = "nickname", required = false) String nickname) {
        return Result.success(userService.page(current, size, nickname));
    }

    @PostMapping("/status/update")
    @PreAuthorize("hasAuthority('console')")
    @LogAnnotation(module="登录服务",operator="修改用户状态,用于禁用、锁定用户等操作")
    @ApiOperation(value = "修改用户状态,用于禁用、锁定用户等操作", notes = "需要accessToken，需要管理员权限")
    public Result status(@ApiParam("用户id") @RequestParam("userId") Integer userId,
                              @ApiParam("状态,0:正常，1:锁定，2:禁用，3:过期") @RequestParam("status") Integer status) {
        userService.status(userId, status);
        return Result.success();
    }
}
