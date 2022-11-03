//package com.zlr.seat.utils;
//
//import com.zlr.seat.entity.pojo.User;
//
///**
// * @author Zenglr
// * @program: seat
// * @packagename: com.zlr.seat.utils
// * @Description
// * @create 2022-09-16-下午4:04
// */
//public class UserThreadLocal {
//
//    private UserThreadLocal(){}
//    //线程变量隔离
//    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();
//
//    public static void put(User User){
//        LOCAL.set(User);
//    }
//
//    public static User get(){
//        return LOCAL.get();
//    }
//
//    public static void remove(){
//        LOCAL.remove();
//    }
//}
