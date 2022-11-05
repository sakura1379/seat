// TODO: 2022/11/5 未改完userinsert
//package com.zlr.seat.utils;
//
///**
// * @author Zenglr
// * @program: seat
// * @packagename: com.zlr.seat.utils
// * @Description
// * @create 2022-11-04-下午5:42
// */
//
//import ch.qos.logback.core.db.dialect.DBUtil;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.zlr.seat.entity.pojo.User;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.io.RandomAccessFile;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class UserUtil {
//
//    private static void createUser(int count) throws Exception{
//        List<User> users = new ArrayList<User>(count);
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        //生成用户
//        for(int i=0;i<count;i++) {
//            User user = new User();
//            //user.setId((int)10000000000L+i);
//            user.setLoginCount(1);
//            user.setNickname("user"+i);
//            user.setRegisterDate(new Date());
//            user.setMobilePhoneNumber((18077200000L+i)+"");
//            user.setLastLoginDate(new Date());
//            user.setHead("");
//            user.setPassword(passwordEncoder.encode("123456"));
//            users.add(user);
//        }
//		System.out.println("create user");
//		//插入数据库
//		Connection conn = DBUtil.getConn();
//		String sql = "INSERT INTO `seckill`.`user` (`user_name`, `phone`, `password`, `salt`, `head`, `login_count`," +
//				" `register_date`, `last_login_date`)values(?,?,?,?,?,?,?,?)";
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		for(int i=0;i<users.size();i++) {
//			User user = users.get(i);
//			//pstmt.setLong(1, user.getId());
//			pstmt.setString(1, user.getNickname();
//			pstmt.setString(2, user.getMobilePhoneNumber();
//			pstmt.setString(3, user.getPassword());
//			pstmt.setString(5, user.getHead());
//			pstmt.setInt(6, user.getLoginCount());
//			pstmt.setTimestamp(7, new Timestamp(user.getRegisterDate().getTime()));
//			pstmt.setTimestamp(8, new Timestamp(user.getRegisterDate().getTime()));
//			pstmt.addBatch();
//		}
//		pstmt.executeBatch();
//		pstmt.close();
//		conn.close();
//		System.out.println("insert to db");
//        //登录，生成token
////        String urlString = "http://localhost:8080/page/login";
//
//        System.out.println("over");
//    }
//
//    public static void main(String[] args)throws Exception {
//        createUser(5000);
//    }
//}
//
