package com.login.dao.Impl;

import com.login.dao.Ilogindao;
import com.login.entity.User;
import com.utils.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogindaoImpl implements Ilogindao {
    public String login(String username,String password) {
        User user = null;
        String sql="select username,password,phoneNumber \n" +
                "from user \n" +
                "where username=? and password=?";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        DbUtil dbUtil;

        try {
            dbUtil=new DbUtil();
            preparedStatement=dbUtil.getConnection().prepareStatement(sql);
            System.out.println("username="+username);
            System.out.println("password="+password);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            rs=preparedStatement.executeQuery();

            while(rs.next()){
//                user=new User(rs.getString("username"),
//                        rs.getString("password"),
//                        rs.getString("phoneNumber"));
                System.out.println("phoneNumber="+rs.getString("phoneNumber"));
                user = new User();
                user.setId("");
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setGender("");
                user.setCareer("");
                user.setAge(-1);
                user.setHobby("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{

            try {
                if(rs!=null)rs.close();
                if(preparedStatement!=null)preparedStatement.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(user != null)
        return user.getPhoneNumber();
        else return null;

    }
    public static void main(String[] args) {
        LogindaoImpl logindao = new LogindaoImpl();
        System.out.println(logindao.login("李佩奇", "123456"));
    }
}
