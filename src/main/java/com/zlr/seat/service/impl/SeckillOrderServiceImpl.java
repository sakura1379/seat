package com.zlr.seat.service.impl;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

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
    StringRedisTemplate stringRedisTemplate;


    @Override
    public OrderInfo getOrderInfo(long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        return orderInfo;
    }

    @Transactional
    @Override
    public OrderInfo insert(User user, Seats seats) {

        return null;
    }

    @Override
    public boolean checkPath(User user, long seatsId, String path) {
        if(user == null || path == null) {
            return false;
        }
        String pathOld = stringRedisTemplate.opsForValue().get(RedisConstant.SECKILL_PATH + user.getId() + "_" + seatsId);
        return path.equals(pathOld);
    }

    @Override
    public String createSecKillPath(User user, long seatsId) {
        if (user == null || seatsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUID.randomUUID() + "123456");
        stringRedisTemplate.opsForValue().set(RedisConstant.SECKILL_PATH + user.getId() + "_" + seatsId,str,RedisConstant.SEATS_ID);
        return str;
    }

    @Override
    public SeckillOrder checkSecKillOrder(long userId, long seatsId) {
        return null;
    }
}
