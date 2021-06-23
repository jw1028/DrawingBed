package org.example.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Util {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 把java对象序列化为json字符串（Servlet响应输出的body需要json字符串）
     */
    public static String serialize(Object object){
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化java对象失败："+ object, e);
        }
    }

    public static Connection getConnection(){
        try {
            return MysqlDataSource.getInstance().getConnection();
//            return DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("数据库连接获取失败", e);
        }
    }

    public static void close(Connection c, Statement s, ResultSet rs){
        try {
            if(rs != null) rs.close();
            if(s != null) s.close();
            if(c != null) c.close();
        } catch (SQLException e) {
            throw new RuntimeException("释放数据库资源失败", e);
        }
    }

    public static void close(Connection c, Statement s){
        close(c, s, null);
    }

}
