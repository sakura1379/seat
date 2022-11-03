package com.zlr.seat.service;

import com.zlr.seat.entity.pojo.OrderInfo;
import com.zlr.seat.entity.pojo.Seats;
import com.zlr.seat.entity.pojo.SeckillOrder;
import com.zlr.seat.entity.pojo.User;

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
     * 新建一个订单
     * @param user
     * @param seats
     * @return
     */
    OrderInfo insert(User user, Seats seats);

    /**
     * 验证path是否正确
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkPath(User user, long goodsId, String path);


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
}
