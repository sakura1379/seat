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
 * @create 2022-11-01-下午4:39
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="OrderInfo对象", description="订单表")
public class OrderInfo {
    /**
     * `id` bigint NOT NULL AUTO_INCREMENT,
     *   `user_id` bigint DEFAULT NULL COMMENT '用户ID',
     *   `seats_id` bigint DEFAULT NULL COMMENT '座位ID',
     *   `seats_area` varchar(16) DEFAULT NULL COMMENT '冗余过来的座位区域',
     *   `seats_count` int DEFAULT '0' COMMENT '座位数量',
     *   `order_channel` tinyint DEFAULT '0' COMMENT '1pc，2android，3ios',
     *   `status` tinyint DEFAULT '0' COMMENT '预订状态，0新建未确认，1已确认，2已完成，3已取消',
     *   `create_date` datetime DEFAULT NULL COMMENT '预订的创建时间',
     *   `use_date` datetime DEFAULT NULL COMMENT '冗余过来的使用时间',
     */

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "座位id")
    private Long seatsId;

    @ApiModelProperty(value = "座位区域")
    private String seatsArea;

    @ApiModelProperty(value = "使用时间")
    private Date useDate;

    @ApiModelProperty(value = "座位数量")
    private Integer seatsCount;

    @ApiModelProperty(value = "1pc，2android，3ios")
    private Integer orderChannel;

    @ApiModelProperty(value = "预订状态，0新建未确认，1已确认，2已完成，3已取消")
    private Integer status;

    @ApiModelProperty(value = "预订的创建时间")
    private Date createDate;

}
