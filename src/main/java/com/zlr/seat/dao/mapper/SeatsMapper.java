package com.zlr.seat.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zlr.seat.entity.pojo.Seats;
import com.zlr.seat.vo.SeatsVo;


import java.util.List;


/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.dao.mapper
 * @Description
 * @create 2022-11-01-下午7:35
 */
public interface SeatsMapper extends BaseMapper<Seats> {

    /**
     * 分页查询秒杀座位列表
     * @param
     * @return
     */
    List<SeatsVo> selectAllSeats(long offset, long limit);

    /**
     * 根据id查询座位详情
     * @param seatsId
     * @return
     */
    SeatsVo selectSeatsVoById(long seatsId);

    /**
     * 减库存
     * @param seatsId
     * @return
     */
    int updateStock(long seatsId);

    /**
     * 查询所有
     * @return
     */
    List<SeatsVo> selectAllList();
}
