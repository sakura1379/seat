package com.zlr.seat.common.constant;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.common.constant
 * @Description
 * @create 2022-10-13-下午3:10
 */
public class RedisConstant {

    /**
     * 短信验证码 key前缀
     */
    public static final String SMS_CODE = "sms:code:";


    /**
     * 限流前缀
     */
    public static final String REDIS_LIMIT_KEY_PREFIX = "limit:";

    /**
     * 短信限流key前缀（命名空间）
     */
    public static final String SMS_LIMIT_NAME ="sms";




    /**
     * path、是否秒杀结束过期时间
     */
    public static final int SEATS_ID = 60;

    /**
     * 是否秒杀结束
     */
    public static final String IS_SEAT_OVER = "IS_SEAT_OVER";

    /**
     * 库存redis缓存
     */
    public static final String SECKILL_STOCK = "SeckillGoodsStock:";


    /**
     * 库存存储、座位列表过期时间
     */
    public static final int SEATS_LIST = 60 * 30 * 24;
}

