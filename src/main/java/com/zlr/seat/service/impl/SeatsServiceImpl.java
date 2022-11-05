package com.zlr.seat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlr.seat.common.sercurity.ServerSecurityContext;
import com.zlr.seat.dao.mapper.SeatsMapper;
import com.zlr.seat.entity.pojo.Seats;
import com.zlr.seat.service.IUserService;
import com.zlr.seat.service.SeatsService;
import com.zlr.seat.vo.SeatsDetailVo;
import com.zlr.seat.vo.SeatsVo;
import com.zlr.seat.vo.StudentUserDetails;
import com.zlr.seat.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service.impl
 * @Description
 * @create 2022-11-02-上午10:14
 */
@Slf4j
@Service
public class SeatsServiceImpl extends ServiceImpl<SeatsMapper, Seats> implements SeatsService {

    @Resource
    SeatsMapper seatsMapper;

    @Resource
    IUserService userService;

    @Override
    public IPage<SeatsVo> getSecKillSeatsList(long current, long size) {
        QueryWrapper<Seats> queryWrapper = new QueryWrapper<>();
        int count = count(queryWrapper);
        if (count == 0) {
            return new Page<>(current, size);
        }
        List<SeatsVo> seatsVoList = seatsMapper.selectAllSeats((current - 1) * size,size);
        Page<SeatsVo> page = new Page<>(current, size, count);
        page.setRecords(seatsVoList);
        return page;
    }

    @Override
    public SeatsDetailVo getSeatsDetailById(long id){
        StudentUserDetails userDetail = ServerSecurityContext.getUserDetail(true);
        long userId = userDetail.getId();
        SeatsVo seatsVo = seatsMapper.selectSeatsVoById(id);
        UserVo userVo = userService.selectUserVoById(userId);
        SeatsDetailVo seatsDetailVo = new SeatsDetailVo();
        seatsDetailVo.setSeats(seatsVo);
        seatsDetailVo.setUser(userVo);
        long startAt = seatsVo.getStartDate().getTime();
        long endAt = seatsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int seckillStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            seckillStatus = 1;
        }
        seatsDetailVo.setRemainSeconds(remainSeconds);
        seatsDetailVo.setSecKillStatus(seckillStatus);
        return seatsDetailVo;
    }

    @Override
    public boolean reduceStock(long seatsId) {
        return seatsMapper.updateStock(seatsId) > 0;
    }

    @Override
    public List<SeatsVo> getAllSecKillSeatsList() {
        return seatsMapper.selectAllList();
    }

    @Override
    public SeatsVo getSeatsVoById(long seatsId) {
        return seatsMapper.selectSeatsVoById(seatsId);
    }
}
