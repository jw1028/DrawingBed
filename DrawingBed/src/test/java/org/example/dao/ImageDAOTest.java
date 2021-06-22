package org.example.dao;

import org.example.model.Image;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Intellig IDEA
 * Description:
 * User: zjc
 * Date: 2021- 06 -22
 * Time: 10:41
 */
public class ImageDAOTest {

    @Test
    public void queryCountByMd5() {
        ImageDAO imageDAO = new ImageDAO();
        int n = imageDAO.queryCountByMd5("c4329f0beed8da1e59c2ba25a35226c3");
        System.out.println(n);
    }

    @Test
    public void insert() {
        ImageDAO imageDAO = new ImageDAO();
        Image image = new Image();
        image.setImageName("7.jpg");
        image.setSize((long)52074);
        image.setUploadTime("2021-06-22 00:00:14");
        image.setMd5("c4329f0beed8da1e59c2ba25a35226c3");
        image.setContentType("image/jpeg");
        image.setPath("/c4329f0beed8da1e59c2ba25a35226c3");
        imageDAO.insert(image);
    }

    @Test
    public void queryAllImage() {
        ImageDAO imageDAO = new ImageDAO();
        List<Image> list = new ArrayList<>();
        list = imageDAO.queryAllImage();
        System.out.println(list.size());
        for(Image image : list) {
            System.out.println(image.getImageName());
        }
    }

    @Test
    public void queryOneImage() {
        ImageDAO imageDAO = new ImageDAO();
        Image image = imageDAO.queryOneImage(5);
        System.out.println(image);

    }

    
    @Test
    public void delete() {
        ImageDAO imageDAO = new ImageDAO();
        int n = imageDAO.delete(19);
        System.out.println(n);
    }
}
