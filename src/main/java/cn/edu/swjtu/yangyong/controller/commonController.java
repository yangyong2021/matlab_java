package cn.edu.swjtu.yangyong.controller;

import cn.edu.swjtu.yangyong.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("markedFile") String markedFile,
                            @RequestParam("optionValue") String optionValue,
                            @RequestParam("files[]") MultipartFile []files) {
        log.info("文件上传={},{},{}",markedFile,optionValue,files);
        for (MultipartFile file : files) {
            // 对每个文件执行操作，如保存到磁盘、处理数据等
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //原始文件名
//            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 使用UUID生成文件名，防止文件名相同覆盖
//            String fileName = UUID.randomUUID().toString() + suffix;
            File dir = new File(basePath);
            if (!dir.exists()){
                dir.mkdir();
            }
            try {
                file.transferTo(new File(basePath + originalFilename));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // file.getInputStream() 可以用于读取文件内容
            // markedFile 是单选框选中的值
        }
        return null;
    }

}
