package com.zlr.seat.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.entity.pojo
 * @Description
 * @create 2022-11-01-下午4:35
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="Seats对象", description="座位表")
public class Seats {
/**
 * `id` bigint NOT NULL AUTO_INCREMENT COMMENT '座位ID',
 *   `seats_area` varchar(16) DEFAULT NULL COMMENT '座位区域',
 *   `seats_floor` varchar(64) DEFAULT NULL COMMENT '座位楼层',
 *   `use_date` datetime DEFAULT NULL COMMENT '使用时间',
 *   `seats_stock` int DEFAULT '0' COMMENT '座位区域库存，-1表示没有限制',
 */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "座位id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "座位区域")
    private String seatsArea;

    @ApiModelProperty(value = "座位楼层")
    private String seatsFloor;

    @ApiModelProperty(value = "使用时间")
    private Date useDate;

    @ApiModelProperty(value = "座位区域库存，-1表示没有限制")
    private Integer seatsStock;
}
