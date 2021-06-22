package org.example.test;//package org.example.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileCopyTest {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("C:\\Users\\86131\\Desktop\\图片\\1.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("E://1.jpg");
        byte[] bytes = new byte[1024];
        int len;
        while((len = inputStream.read(bytes)) != -1){
            fileOutputStream.write(bytes, 0, len);
        }
        inputStream.close();
        fileOutputStream.close();
    }
}
