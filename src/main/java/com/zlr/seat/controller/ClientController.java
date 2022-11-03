package com.zlr.seat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.entity.pojo.Client;
import com.zlr.seat.exception.GlobleException;
import com.zlr.seat.service.IClientService;
import com.zlr.seat.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import jdk.nashorn.internal.objects.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.controller
 * @Description
 * @create 2022-10-20-下午4:57
 */
@RestController
@RequestMapping("/client")
@Api(tags = "客户端服务",value = "/client")
public class ClientController {
    @Resource
    private IClientService clientService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation(value = "分页获取客户端列表",notes = "需要accessToken，需要管理员权限")
    public Result<IPage<Client>> page(@ApiParam("页码") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
                                      @ApiParam("每页数量") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
        return Result.success(clientService.page(new Page<>(current, size)));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除客户端",notes = "需要accessToken，需要管理员权限")
    public Result delete(@ApiParam("id") @PathVariable(value = "id") int id) {
        clientService.removeById(id);
        clientService.clearCache();
        return Result.success();
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增或更新客户端,id为null时新增",notes = "需要accessToken，需要管理员权限")
    public Result save(@Validated @RequestBody Client client) {
        validateExist(client);
        clientService.saveOrUpdate(client);
        clientService.clearCache();
        return Result.success();
    }

    /**
     * 校验是否已存在
     * @param client
     */
    private void validateExist(Client client) {
        if (client.getId() == null) {
            QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Client::getClientId,client.getClientId());
            int count = clientService.count(queryWrapper);
            if (count != 0) {
                throw new GlobleException(ResultStatus.CLIENT_ERROR);
            }
        }
    }
}
