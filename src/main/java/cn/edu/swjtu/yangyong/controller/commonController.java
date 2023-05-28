package cn.edu.swjtu.yangyong.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author Yangyong
 * 2023-05-28 6:52
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class commonController {
    @Value("${matlab-java.path}")
    private String basePath;

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        log.info("图片名称：{}",name);
        try{
            FileInputStream fileInputStream = new FileInputStream(new File((basePath + name+".png")));

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
