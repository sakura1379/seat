package com.zlr.seat.common.constant;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.common.aop
 * @Description
 * @create 2022-09-16-下午7:51
 */
public class AuthConst {

    /**
     *  私有构造方法
     */
    private AuthConst() {
    }


    // --------------- 代表身份的权限 ---------------

    public static final Integer ADMIN = 1; 			 // 管理员
    public static final Integer STUDENT = 2;		  // 学生（普通用户）
    public static final Integer SUPER_ADMIN = 11; 		 //最高权限，超管身份的代表


    // --------------- 所有权限码 ---------------

    public static final String AUTH = "auth";		   // 权限管理
    public static final String ROLE_LIST = "role-list";		    // 权限管理 - 角色管理
    public static final String MENU_LIST = "menu-list";		   // 权限管理 - 菜单列表
    public static final String ADMIN_LIST = "admin-list";		   // 权限管理 - 管理员列表
    public static final String ADMIN_ADD = "admin-add";		   // 权限管理 - 管理员添加

    public static final String CONSOLE = "console";		   // 监控中心
    public static final String SQL_CONSOLE = "sql-console";		      // 监控中心 - SQL监控
    public static final String REDIS_CONSOLE = "redis-console";		   // 监控中心 - Redis 控制台
    public static final String APILOG_LIST = "apilog-list";		   // 监控中心 - API 请求日志

    public static final String SEAT_INFO = "seat-info";    //座位信息表
    public static final String MIAO_SHA_INFO = "miao-sha-info";  //抢座表

}
