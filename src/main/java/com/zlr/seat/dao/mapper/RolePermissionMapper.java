package com.zlr.seat.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlr.seat.entity.pojo.OauthUser;
import com.zlr.seat.entity.pojo.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.dao.mapper
 * @Description
 * @create 2022-09-26-下午5:32
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 根据roleId查询权限码列表
     * @param
     * @return
     */
    List<String> selectPermissionCode(@Param("roleId") Integer roleId);
}
