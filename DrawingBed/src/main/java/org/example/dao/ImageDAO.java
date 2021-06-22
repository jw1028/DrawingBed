package org.example.dao;

import org.example.exception.AppException;
import org.example.model.Image;
import org.example.servlet.ImageServlet;
import org.example.util.Util;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {

    public static int queryCountByMd5(String md5) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //1. 获取数据库连接Connection
            c = Util.getConnection();
            //2. 获取操作命令对象Statement（connection+sql）
            String sql = "select count(0) cnt from image_table where md5=?";
            ps = c.prepareStatement(sql);
            //3. 执行sql: 执行前替换占位符
            ps.setString(1, md5);
            rs = ps.executeQuery();

            //4. 如果是查询语句，需要处理结果集ResultSet
                rs.next();
                return rs.getInt("cnt");
        }catch (SQLException e){
            throw new RuntimeException("查询上传图片md5出错："+ md5, e);
        }finally {
            //5. 释放资源
            Util.close(c, ps, rs);
        }
    }

    public static int insert(Image image) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            //1. 获取数据库连接Connection
            c = Util.getConnection();
            //2. 获取操作命令对象Statement（connection+sql）
            String sql = "insert into image_table values(null, ?,?,?,?,?,?)";
            ps = c.prepareStatement(sql);
            //3. 执行sql: 执行前替换占位符
            ps.setString(1, image.getImageName());
            ps.setLong(2, image.getSize());
            ps.setString(3, image.getUploadTime());
            ps.setString(4, image.getMd5());
            ps.setString(5, image.getContentType());
            ps.setString(6, image.getPath());
            return ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("新增上传图片出错", e);
        }finally {
            //5. 释放资源
            Util.close(c, ps);
        }
    }

    public static List<Image> queryAllImage() {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = Util.getConnection();
            String sql = "select * from image_table";
            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();
            List<Image> list = new ArrayList<>();
            while (rs.next()){
                Image image = new Image();
                image.setImageId(rs.getInt("image_id"));
                image.setImageName(rs.getString("image_name"));
                image.setContentType(rs.getString("content_type"));
                image.setMd5(rs.getString("md5"));
                list.add(image);
            }
            return list;
        }catch (SQLException e){
            throw new RuntimeException("查询所有图片出错", e);
        }finally {
            Util.close(c, ps, rs);
        }
    }

    public static Image queryOneImage(int id) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            c = Util.getConnection();
            String sql = "select * from image_table where image_id=?";
            ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            Image image = null;
            while (rs.next()){
                image = new Image();
                image.setImageId(rs.getInt("image_id"));
                image.setImageName(rs.getString("image_name"));
                image.setContentType(rs.getString("content_type"));
                image.setMd5(rs.getString("md5"));
                image.setPath(rs.getString("path"));
            }
            return image;
        }catch (SQLException e){
            throw new RuntimeException("查询所有图片出错", e);
        }finally {
            Util.close(c, ps, rs);
        }
    }

    public static int delete(int id) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            //1获取Connection
            c = Util.getConnection();
            //不自动提交（开启事务）
            c.setAutoCommit(false);
            //2获取Statement
            String sql = "delete from image_table where image_id=?";
            ps = c.prepareStatement(sql);
            //3执行sql
            ps.setInt(1, id);
            int n = ps.executeUpdate();

            //本地硬盘删除图片文件
            Image image = ImageDAO.queryOneImage(id);
            String path = ImageServlet.IMAGE_DIR + image.getPath();
            File file = new File(path);
            file.delete();
            c.commit();
            return n;
        }catch (Exception e){
            try {
                c.rollback();
            } catch (SQLException se) {
                throw new AppException("删除图片回滚出错:"+id, se);
            }
            throw new AppException("删除图片出错:"+id, e);
        }finally {
            //5释放资源
            Util.close(c, ps);
        }
    }
}
