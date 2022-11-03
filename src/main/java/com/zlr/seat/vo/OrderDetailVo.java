package com.zlr.seat.vo;

import com.zlr.seat.entity.pojo.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.vo
 * @Description
 * @create 2022-11-02-上午10:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {

    private SeatsVo seats;
    private OrderInfo order;
}
