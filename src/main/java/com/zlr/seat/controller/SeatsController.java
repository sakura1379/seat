package com.zlr.seat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zlr.seat.common.aop.LogAnnotation;
import com.zlr.seat.service.SeatsService;
import com.zlr.seat.vo.Result;
import com.zlr.seat.vo.SeatsDetailVo;
import com.zlr.seat.vo.SeatsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.controller
 * @Description
 * @create 2022-11-02-下午4:41
 */
@Slf4j
@RestController
@RequestMapping("/seat")
@Api(tags = "座位服务",value = "/seat")
public class SeatsController {

    @Resource
    SeatsService seatsService;

    @GetMapping("/page")
    @LogAnnotation(module="座位服务",operator="分页查询所有可秒杀的座位列表")
    @ApiOperation(value = "分页查询所有可秒杀的座位列表",notes = "不需要accessToken")
    public Result<IPage<SeatsVo>> page(
            @ApiParam("当前页，默认值：1") @RequestParam(value = "current", required = false, defaultValue = "1") long current,
            @ApiParam("每页数量，默认值为：5") @RequestParam(value = "size", required = false, defaultValue = "5") long size) {
        return Result.success(seatsService.getSecKillSeatsList(current,size));
    }


    @GetMapping("/detail")
    @LogAnnotation(module="座位服务",operator="根据id查询秒杀座位详细信息")
    @ApiOperation(value = "根据id查询秒杀座位详细信息",notes = "需要accessToken")
    public Result<SeatsDetailVo> detail(
            @ApiParam("座位Id") @RequestParam(value = "seatsId") long seatsId) {
        return Result.success(seatsService.getSeatsDetailById(seatsId));
    }
}
