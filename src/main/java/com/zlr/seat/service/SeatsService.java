package com.zlr.seat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zlr.seat.vo.SeatsDetailVo;
import com.zlr.seat.vo.SeatsVo;

import java.util.List;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service
 * @Description
 * @create 2022-11-02-上午10:14
 */
public interface SeatsService {

    /**
     * 获得秒杀的座位列表信息
     * @return
     */
    IPage<SeatsVo> getSecKillSeatsList(long current, long size);

    /**
     * 秒杀详情页
     * @param id
     * @return
     */
    SeatsDetailVo getSeatsDetailById(long id);


    /**
     * 减库存
     * @param seatsId
     * @return
     */
    int reduceStock(long seatsId);
}
