package org.example.servlet;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.dao.ImageDAO;
import org.example.exception.AppException;
import org.example.model.Image;
import org.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 1. @WebServlet
 * 2. 继承HttpServlet
 * 3. 重写doXXX方法
 */
@WebServlet("/image")
@MultipartConfig
public class ImageServlet extends HttpServlet {

    public static final String IMAGE_DIR = "E:\\Javacode\\DrawingBed\\TmpPictures";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        //构造要返回的响应体信息，也可以创建一个类
        Map<String, Object> map = new HashMap<>();

        try {
            //1. 解析请求数据
            Part part = req.getPart("uploadImage");
            //获取上传的文件大小
            long size = part.getSize();
            //获取每个part的数据格式
            String contentType = part.getContentType();
            //获取上传的文件名
            String name = part.getSubmittedFileName();
            //图片上传时间，数据库是保存的字符串，所以用日期格式化类来转换
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String uploadTime = dateFormat.format(date);
            //获取part（上传图片文件）的输入流
            InputStream inputStream = part.getInputStream();//获取上传文件的输入流（数据）
            //根据输入流转md5校验码
            String md5 = DigestUtils.md5Hex(inputStream);

            //如果已上传该图片（相同的md5），就不能插入数据和保存本地
            int num = ImageDAO.queryCountByMd5(md5);
            if(num >= 1){
                throw new AppException("上传图片重复");
            }

            //2. 根据请求数据完成业务处理
            //2-1: 保存上传图片为服务端本地文件
            //上传的图片名可能重复，但md5是唯一的
            part.write(IMAGE_DIR + "/" + md5);
            //2-2: 图片信息保存在数据库---->后续查询图片列表接口要用
            //插入数据操作，字段太多，最好是把字段转为对象的属性
            Image image = new Image();
            image.setImageName(name);
            image.setContentType(contentType);
            image.setSize(size);
            image.setUploadTime(uploadTime);
            image.setMd5(md5);
            image.setPath("/" + md5);
            int n = ImageDAO.insert(image);
            map.put("ok", true);
        }catch (Exception e){
            e.printStackTrace();
            map.put("ok", false);
            if(e instanceof AppException){
                map.put("msg", e.getMessage());
            }else{
                map.put("msg", "未知错误，请联系管理员");
            }
        }
        //3. 返回响应数据
        String response = Util.serialize(map);
        resp.getWriter().println(response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String id = req.getParameter("imageId");
        Object result = null;
        //查询所有图片: result = List<Image>
        if(id == null){
            result = ImageDAO.queryAllImage();
        }else{//查询指定id的一个图片: result = image对象
            result = ImageDAO.queryOneImage(Integer.parseInt(id));
        }
        String response = Util.serialize(result);
        resp.getWriter().println(response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String id = req.getParameter("imageId");
        //数据库删除和本地硬盘删除使用事务保证ACID
        //数据库根据id删除图片数据
        int n = ImageDAO.delete(Integer.parseInt(id));
        resp.getWriter().println("{\"ok\":true}");
    }
}
