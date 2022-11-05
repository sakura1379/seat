package com.zlr.seat.mq;

import com.alibaba.fastjson.JSON;
import com.zlr.seat.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.mq
 * @Description
 * @create 2022-11-04-上午12:55
 */
@Slf4j
@Service
public class MQSender {
    @Resource
    AmqpTemplate amqpTemplate;

    public void sendSeckillMessage(SeckillMessage mm) {
        String msg = JSON.toJSONString(mm);
        log.info("send message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, mm);
    }
}
