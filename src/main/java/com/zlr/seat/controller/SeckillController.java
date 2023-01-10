package com.zlr.seat.controller;

import com.alibaba.fastjson.JSON;
import com.zlr.seat.common.aop.LogAnnotation;
import com.zlr.seat.common.constant.RedisConstant;
import com.zlr.seat.common.limit.RateLimiter;
import com.zlr.seat.common.sercurity.ServerSecurityContext;
import com.zlr.seat.config.MQConfig;
import com.zlr.seat.config.RedisConfig;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.entity.pojo.OrderInfo;
import com.zlr.seat.entity.pojo.Seats;
import com.zlr.seat.entity.pojo.SeckillOrder;
import com.zlr.seat.entity.pojo.User;
import com.zlr.seat.exception.GlobleException;
import com.zlr.seat.mq.MQSender;
import com.zlr.seat.mq.SeckillMessage;
import com.zlr.seat.service.IUserService;
import com.zlr.seat.service.SeatsService;
import com.zlr.seat.service.SeckillOrderService;
import com.zlr.seat.vo.OrderDetailVo;
import com.zlr.seat.vo.Result;
import com.zlr.seat.vo.SeatsVo;
import com.zlr.seat.vo.StudentUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.SynthScrollBarUI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.controller
 * @Description
 *
 * @create 2022-11-03-下午3:46
 */
@Slf4j
@RestController
@RequestMapping("/seckill")
@Api(tags = "秒杀服务",value = "/seckill")
public class SeckillController implements InitializingBean {

    @Resource
    SeatsService seatsService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    SeckillOrderService seckillOrderService;
    @Resource
    IUserService userService;
    @Resource
    MQSender mqSender;

    @Resource
    RedisConfig redisConfig;

    /**
     * 如果是集群情况下，需要达到一定量此缓存才能起到重大作用
     * 判断是否还有库存
     */
    private final HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 将库存初始化到本地缓存及redis缓存
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<SeatsVo> seatsList = seatsService.getAllSecKillSeatsList();
        if(seatsList == null){
            return;
        }
        for(SeatsVo seat : seatsList){
            stringRedisTemplate.opsForValue().set(RedisConstant.SECKILL_STOCK + seat.getId(),
                    String.valueOf(seat.getStockCount()),720L, TimeUnit.MINUTES);
            localOverMap.put(seat.getId(), false);
        }
    }

    @PostMapping("/{path}/seckill")
    @LogAnnotation(module="秒杀服务",operator="秒杀座位提交订单")
    @ApiOperation(value = "秒杀座位提交订单",notes = "需要accessToken")
    public Result seckillOrder(@ApiParam("座位Id") @RequestParam(value = "seatsId") long seatsId,
                       @ApiParam("秒杀path") @RequestParam(value = "path") String path){
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        User user = userService.getById(userDetail.getId());
        //验证path
//        boolean check = seckillOrderService.checkPath(user, seatsId, path);
//        if(!check){
//            return Result.error(ResultStatus.ACCESS_LIMIT_REACHED);
//        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(seatsId);
        if (over) {
            return Result.error(ResultStatus.ORDER_OVER);
        }
        //预减库存
//        long stock = stringRedisTemplate.opsForValue().decrement(RedisConstant.SECKILL_STOCK+seatsId);

        Long stock = stringRedisTemplate.execute(redisConfig.stockScript(), Collections.singletonList(RedisConstant.SECKILL_STOCK + seatsId), Collections.EMPTY_LIST.toString());
        if (stock <= 0) {
            localOverMap.put(seatsId, true);
            return Result.error(ResultStatus.ORDER_OVER);
        }
        //判断是否重复秒杀
        SeckillOrder order = seckillOrderService.checkSecKillOrder(user.getId(), seatsId);
        if (order != null) {
            return Result.error(ResultStatus.REPEATE_ORDER);
        }
        //入队 下订单
        SeckillMessage mm = new SeckillMessage();
        mm.setUser(user);
        mm.setSeatsId(seatsId);
        mqSender.sendSeckillMessage(mm);
        return Result.success();
    }


    @RateLimiter(name = "getSeckillPath",max = 5,key = "#seatsId", timeout = 5L)
    @GetMapping("/path")
    @LogAnnotation(module="秒杀服务",operator="获取秒杀座位提交订单地址")
    @ApiOperation(value = "获取秒杀座位提交订单地址",notes = "需要accessToken")
    public Result<String> getSeckillPath(@ApiParam("座位Id") @RequestParam(value = "seatsId") long seatsId) {
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        User user = userService.getById(userDetail.getId());
        String path = seckillOrderService.createSecKillPath(user, seatsId);
        return Result.success(path);
    }

    /**
     * 客户端轮询查询是否下单成功
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @GetMapping("/result")
    @LogAnnotation(module="秒杀服务",operator="客户端轮询查询是否下单成功")
    @ApiOperation(value = "客户端轮询查询是否下单成功",notes = "需要accessToken,返回orderId：成功、-1：秒杀失败、0： 排队中")
    public Result<Long> checkSeckillResult(@ApiParam("座位Id") @RequestParam(value = "seatsId") long seatsId) {
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        long result = seckillOrderService.getSeckillResult(userDetail.getId(),seatsId);
        return Result.success(result);
    }

    @GetMapping("/orderDetail")
    @LogAnnotation(module="秒杀服务",operator="查询订单详情")
    @ApiOperation(value = "查询订单详情",notes = "需要accessToken")
    public Result<OrderDetailVo> getOrderDetail(@ApiParam("订单Id") @RequestParam(value = "orderId") long orderId){
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        User user = userService.getById(userDetail.getId());
        OrderInfo orderInfo = seckillOrderService.getOrderInfo(orderId);
        long seatsId = orderInfo.getSeatsId();
        SeatsVo seatsVo = seatsService.getSeatsVoById(seatsId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(orderInfo);
        orderDetailVo.setSeats(seatsVo);
        return Result.success(orderDetailVo);
    }

    /**
     * redis更新后发送消息给到mysql更新库存量
     * TODO 如果这里出现异常可以进行补偿，重试，重新执行此逻辑，如果超过一定次数还是失败可以将此秒杀置为无效，恢复redis库存
     * @param sm
     */
    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void refreshStockInMysql(SeckillMessage sm){
        log.info("receive message:" + JSON.toJSONString(sm));
        User user = sm.getUser();
        long seatsId = sm.getSeatsId();
        SeatsVo seatsVo = seatsService.getSeatsVoById(seatsId);
        int stock = seatsVo.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.checkSecKillOrder(user.getId(), seatsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        seckillOrderService.seckillOrder(user,seatsVo);

    }
}
