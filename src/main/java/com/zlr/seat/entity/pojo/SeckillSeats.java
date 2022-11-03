package com.zlr.seat.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.entity.pojo
 * @Description
 * @create 2022-11-01-下午7:01
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="SeckillSeats对象", description="秒杀座位表")
public class SeckillSeats {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "秒杀座位id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "座位id")
    private Long seatsId;

    @ApiModelProperty(value = "座位库存")
    private Integer stockCount;

    @ApiModelProperty(value = "秒杀开始时间")
    private Date startDate;

    @ApiModelProperty(value = "秒杀结束时间")
    private Date endDate;
}
