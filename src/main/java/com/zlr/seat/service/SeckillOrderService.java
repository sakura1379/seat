package com.zlr.seat.service;

import com.zlr.seat.entity.pojo.OrderInfo;
import com.zlr.seat.entity.pojo.Seats;
import com.zlr.seat.entity.pojo.SeckillOrder;
import com.zlr.seat.entity.pojo.User;
import com.zlr.seat.vo.SeatsVo;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service
 * @Description
 * @create 2022-11-02-下午10:08
 */
public interface SeckillOrderService {

    /**
     * 查询单个订单详情
     * @param orderId
     * @return
     */
    OrderInfo getOrderInfo(long orderId);

    /**
     * 秒杀下订单
     * @param user
     * @param seatsVo
     * @return
     */
    OrderInfo seckillOrder(User user, SeatsVo seatsVo);

    /**
     * 新建一个订单
     * @param user
     * @param seats
     * @return
     */
    OrderInfo insert(User user, Seats seats);

    /**
     * 查看是否秒杀成功
     * @param userId
     * @param seatsId
     * @return
     */
    long getSeckillResult(long userId, long seatsId);

    /**
     * 验证path是否正确
     * @param user
     * @param seatsId
     * @param path
     * @return
     */
    boolean checkPath(User user, long seatsId, String path);


    /**
     * 新建path
     * @param user
     * @param seatsId
     * @return
     */
    String createSecKillPath(User user, long seatsId);

    /**
     * 验证订单是否存在
     * @param userId
     * @param seatsId
     * @return
     */
    SeckillOrder checkSecKillOrder(long userId, long seatsId);


    /**
     * 设置秒杀结束标志
     * @param seatsId
     */
    void setSeatsOver(Long seatsId);

    /**
     * 查看秒杀商品是否已经结束
     * @param seatsId
     * @return
     */
    boolean getSeatsOver(long seatsId);
}
