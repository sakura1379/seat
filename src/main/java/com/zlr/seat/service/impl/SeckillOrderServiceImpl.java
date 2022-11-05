package com.zlr.seat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlr.seat.common.constant.RedisConstant;
import com.zlr.seat.dao.mapper.OrderInfoMapper;
import com.zlr.seat.dao.mapper.SeatsMapper;
import com.zlr.seat.dao.mapper.SeckillOrderMapper;
import com.zlr.seat.entity.pojo.OrderInfo;
import com.zlr.seat.entity.pojo.Seats;
import com.zlr.seat.entity.pojo.SeckillOrder;
import com.zlr.seat.entity.pojo.User;
import com.zlr.seat.service.SeatsService;
import com.zlr.seat.service.SeckillOrderService;
import com.zlr.seat.utils.MD5Util;
import com.zlr.seat.vo.SeatsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service.impl
 * @Description
 * @create 2022-11-02-下午10:08
 */
@Slf4j
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements SeckillOrderService {

    @Resource
    OrderInfoMapper orderInfoMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    SeatsService seatsService;

    /**
     * 秒杀Path前缀
     */
    private final static String SECKILL_PATH = "SecKillPath:";

    @Override
    public OrderInfo getOrderInfo(long orderId) {
        log.info("SeckillOrderService-getOrderInfo-receive:" + orderId);
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        log.info("SeckillOrderService-getOrderInfo-res:" + orderInfo.toString());
        return orderInfo;
    }

    @Override
    public OrderInfo seckillOrder(User user, SeatsVo seatsVo) {
        log.info("SeckillOrderService-seckillOrder-receive:" + user.toString()+seatsVo.toString());
        //减库存 下订单 写入秒杀订单
        boolean success = seatsService.reduceStock(seatsVo.getId());
        if (success) {
            log.info("SeckillOrderService-seckillOrder-reduceStock-success");
            return insert(user,seatsVo);
        } else {
            //如果库存不存在则内存标记为true
            setSeatsOver(seatsVo.getId());
            return null;
        }
    }

    @Transactional
    @Override
    public OrderInfo insert(User user, Seats seats) {
        log.info("insertOrder:"+seats.getId()+"userId:"+user.getId());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setSeatsArea(seats.getSeatsArea());
        orderInfo.setSeatsCount(1);
        orderInfo.setSeatsId(seats.getId());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderInfoMapper.insert(orderInfo);
        log.info("orderId -->" + orderId + "");
        SeckillOrder seckillOrder =  new SeckillOrder();
        seckillOrder.setSeatsId(seats.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        save(seckillOrder);
        return orderInfo;
    }

    @Override
    public long getSeckillResult(long userId, long seatsId) {
        log.info("getSeckillResult:"+seatsId+"userId:"+userId);
        SeckillOrder order = checkSecKillOrder(userId, seatsId);
        if (order != null) {//秒杀成功
            return order.getOrderId();
        } else {
            boolean isOver = getSeatsOver(seatsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public boolean checkPath(User user, long seatsId, String path) {
        if(user == null || path == null) {
            return false;
        }
        log.info("checkSecKillPath:"+seatsId+"userId:"+user.getId()+"path:"+path);
        String pathOld = stringRedisTemplate.opsForValue().get(SECKILL_PATH + user.getId() + "_" + seatsId);
        log.info("pathOld:"+pathOld);
        return path.equals(pathOld);
    }

    @Override
    public String createSecKillPath(User user, long seatsId) {
        if (user == null || seatsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUID.randomUUID() + "123456");
        log.info("createSecKillPath:"+seatsId+"userId:"+user.getId()+"path:"+str);
        stringRedisTemplate.opsForValue().set(SECKILL_PATH + user.getId() + "_" + seatsId, str, 1L, TimeUnit.MINUTES);
        return str;
    }

    @Override
    public SeckillOrder checkSecKillOrder(long userId, long seatsId) {
        log.info("checkSecKillOrder:"+seatsId+"userId:"+userId);
        QueryWrapper<SeckillOrder> queryWrapper =  new QueryWrapper<>();
        queryWrapper.lambda().eq(SeckillOrder::getUserId,userId).eq(SeckillOrder::getSeatsId,seatsId);
        return getOne(queryWrapper);
    }

    @Override
    public void setSeatsOver(Long seatsId) {
        log.info("setSeatsOver:"+seatsId);
        //不能引用RedisConstant.SEATS_ID作为过期时间 会变成二进制 不知道为啥
        stringRedisTemplate.opsForValue().set(RedisConstant.IS_SEAT_OVER + seatsId, "true", 1L, TimeUnit.MINUTES);
    }

    @Override
    public boolean getSeatsOver(long seatsId) {
        boolean res = stringRedisTemplate.hasKey(RedisConstant.IS_SEAT_OVER + seatsId);
        log.info("getSeatsOver:"+seatsId + "res:" + res);
        return res;
    }
}
