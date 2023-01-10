package com.zlr.seat.utils;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.utils
 * @Description
 * @create 2022-11-04-下午5:42
 */

import ch.qos.logback.core.db.dialect.DBUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlr.seat.common.constant.AuthConst;
import com.zlr.seat.common.constant.UserConstant;
import com.zlr.seat.entity.pojo.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {

    private static void createUser(int count) throws Exception{
        List<User> users = new ArrayList<User>(count);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //生成用户
        for(int i=0;i<count;i++) {
            User user = new User();
            user.setId(100000000L+i);
            user.setLoginCount(1);
            user.setNickname("user"+i);
            user.setRoleId(AuthConst.STUDENT);
            user.setRegisterDate(new Date());
            user.setMobilePhoneNumber((18077200000L+i)+"");
            user.setLastLoginDate(new Date());
            user.setHead("");
            user.setStatus(UserConstant.STATUS_NORMAL);
            user.setPassword(passwordEncoder.encode("123456"));
            users.add(user);
        }
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://" +
                "localhost:3306/seat_sys", "root", "201379");
        // 关闭事务自动提交
        conn.setAutoCommit(false);
        System.out.println("create user");
        String sql = "INSERT INTO `seat_sys`.`user` (`role_id`, `mobile_phone_number`, `nickname`, `password`, `head`, `status`, `login_count`," +
				" `register_date`, `last_login_date`,`id`)values(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for(int i=0;i<users.size();i++) {
			User user = users.get(i);
			pstmt.setInt(1, user.getRoleId());
            pstmt.setString(2, user.getMobilePhoneNumber());
			pstmt.setString(3, user.getNickname());
			pstmt.setString(4, user.getPassword());
			pstmt.setString(5, user.getHead());
            pstmt.setInt(6, user.getStatus());
			pstmt.setInt(7, user.getLoginCount());
			pstmt.setTimestamp(8, new Timestamp(user.getRegisterDate().getTime()));
			pstmt.setTimestamp(9, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.setLong(10, user.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();
        conn.commit();
        pstmt.close();
		conn.close();
		System.out.println("insert to db");
        //登录，生成token
        String urlString = "http://localhost:8888/account/login";
        File file = new File("/Users/sskura/Documents/seat/src/main/resources/tokens.txt");
        if(file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for(int i=0;i<users.size();i++) {
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection)url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "username="+user.getMobilePhoneNumber()+"&password="+"123456";
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0 ,len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            System.out.println(jo);
            JSONObject data = jo.getJSONObject("data");
            String token = data.getString("access_token");
            System.out.println("create token : " + user.getId());

            String row = user.getId()+","+token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();

        System.out.println("over");
    }

    public static void main(String[] args)throws Exception {
        createUser(1000);
    }
}

