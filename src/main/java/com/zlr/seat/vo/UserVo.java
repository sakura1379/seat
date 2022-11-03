package com.zlr.seat.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zlr.seat.entity.pojo.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.vo
 * @Description
 * @create 2022-09-16-下午4:06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="UserVo对象", description="返回的用户信息")
public class UserVo extends User {

    @ApiModelProperty(value = "权限码列表")
    protected List<String> permissionCode;
}
