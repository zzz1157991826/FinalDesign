package com.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class DbUtil {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;


    /**
     * 加载配置文件
     */ {
        loadResource();
    }

    /**
     * 读取配置文件
     */
    public static void loadResource() {

        InputStream in = DbUtil.class.getResourceAsStream("/db.properties");
        Properties properties = new Properties();

        try {
            properties.load(in);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (in != null) in.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }

    }

    /**
     * 数据库连接类
     */
    public Connection getConnection() {

        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }/*finally{
			try {
				if(connection!=null)connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/

        return connection;
    }

//    public static void main(String[] args) {
//        DbUtil dbUtil = new DbUtil();
//        dbUtil.getConnection();
//    }
}
