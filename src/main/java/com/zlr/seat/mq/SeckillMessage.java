package com.zlr.seat.mq;

import com.zlr.seat.entity.pojo.User;
import lombok.Data;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.entity.pojo
 * @Description
 * @create 2022-11-04-上午12:47
 */
@Data
public class SeckillMessage {
    private User user;

    private long seatsId;

}
