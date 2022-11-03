package com.zlr.seat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.vo
 * @Description 详情页返回
 * @create 2022-11-02-上午11:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatsDetailVo {
    private int secKillStatus = 0;
    private int remainSeconds = 0;
    private SeatsVo seats;
    private UserVo user;
}
