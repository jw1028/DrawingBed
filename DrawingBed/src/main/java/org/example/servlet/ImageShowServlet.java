package org.example.servlet;

import org.example.dao.ImageDAO;
import org.example.model.Image;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/imageShow")
public class ImageShowServlet extends HttpServlet {

    private static final Set<String> whiteList = new HashSet<>();

    static {//白名单允许获取图片内容：还可以设计为白名单+黑名单的方式
        whiteList.add("http://localhost:8080/DBed/");
        whiteList.add("http://localhost:8080/DBed/index.html");
        whiteList.add("https://github.com/jw1028/Notes/blob/master/Linux/Linux.md/");
        whiteList.add(" https://github.com/jw1028/Notes/blob/master/Linux/Linux.md");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String referer = req.getHeader("Referer");
        //白名单不包含当前请求的Referer，不允许访问
        if(!whiteList.contains(referer)){
            resp.setStatus(403);//权限不够
            return;
        }
        //1解析请求数据：imageId
        String id = req.getParameter("imageId");
        //2业务处理：
        // (1)根据id查图片path字段
        // (2)通过path找本地图片文件
        Image image = ImageDAO.queryOneImage(Integer.parseInt(id));
        //图片是以二进制数据放在body，同时要指定Content-Type
        resp.setContentType(image.getContentType());
        //本地图片的绝对路径
        String path = ImageServlet.IMAGE_DIR + image.getPath();
        //io输入流读文件
        FileInputStream fileInputStream = new FileInputStream(path);
        //3返回响应：服务器本地图片的二进制数据
        //输出流都是输出到body
        OutputStream outputStream = resp.getOutputStream();
        byte[] bytes = new byte[1024 * 8];
        int len;
        while((len = fileInputStream.read(bytes)) != -1){
            outputStream.write(bytes, 0, len);
        }
        //一定要关闭io流，防止被占用而删不掉
        outputStream.flush();
        fileInputStream.close();
        outputStream.close();
    }
}
