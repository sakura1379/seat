package com.zlr.seat.vo;

import com.zlr.seat.entity.pojo.Seats;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.vo
 * @Description
 * @create 2022-11-02-上午10:36
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SeatsVo extends Seats {
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
