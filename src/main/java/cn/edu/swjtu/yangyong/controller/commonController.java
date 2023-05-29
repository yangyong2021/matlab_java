package cn.edu.swjtu.yangyong.controller;

import cn.edu.swjtu.yangyong.common.R;
import cn.edu.swjtu.yangyong.entity.Datasets;
import cn.edu.swjtu.yangyong.entity.Methods;
import cn.edu.swjtu.yangyong.service.DatasetsService;
import cn.edu.swjtu.yangyong.service.MethodsService;
import com.mathworks.engine.MatlabEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private MethodsService methodsService;

    @Autowired
    private DatasetsService datasetsService;

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        log.info("图片名称：{}", name);
        try {
            FileInputStream fileInputStream = new FileInputStream(new File((basePath + name)));

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/upload")
    public R<String> upload(@RequestParam("markedFile") String markedFile,
                            @RequestParam("optionValue") String optionValue,
                            @RequestParam("files[]") MultipartFile[] files) {
        Methods methods = new Methods();
        log.info("文件上传={},{},{}", markedFile, optionValue, files);
        //1. 处理方法名的封装，保存到methods表中的name中去
        // 使用正则表达式匹配大写字母
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(markedFile);
        // 构建大写字母的子字符串
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            sb.append(matcher.group());
        }
        String methodName = sb.toString();
        methods.setName(methodName);

        // 2. 处理主函数，存到表中的main_Name中去
        int dotIndex = markedFile.indexOf('.');
        String subStr = markedFile.substring(0, dotIndex);
        methods.setMainName(subStr);
        // 3. 处理其他函数，存到表中methods字段
        String otherMethods = "";

        for (MultipartFile file : files) {
            // 对每个文件执行操作，如保存到磁盘、处理数据等
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            otherMethods += originalFilename;
            File dir = new File(basePath);
            if (!dir.exists()) {
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
        methods.setMethods(otherMethods);

        //4. 处理后续生成的收敛性图
        String image = methodName + "-" + optionValue + ".png";
        methods.setImage(image);

        // 5. 存到数据库中去
        methodsService.save(methods);

        return R.success(subStr);
    }

    @PostMapping("/uploadMat")
    public R<String> uploadMat(@RequestParam("files[]") MultipartFile[] files) {
        Datasets datasets = new Datasets();
        log.info("文件上传={}", files);
        for (MultipartFile file : files) {
            // 对每个文件执行操作，如保存到磁盘、处理数据等
            // 1. 获得上传的xxx.mat 文件名
            String originalFilename = file.getOriginalFilename();
            // 2. 获得.之前的名称
            int dotIndex = originalFilename.indexOf('.');
            String str = originalFilename.substring(0, dotIndex);

            // 3. 转为大写字母
            String firstChar = str.substring(0, 1).toUpperCase();

            String remainingChars = str.substring(1);

            String result = firstChar + remainingChars;

            // 路径不存在则创建
            File dir = new File(basePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            try {
                file.transferTo(new File(basePath + originalFilename));
                // 4. 通过Matlab获得数据集的：n d c
                MatlabEngine eng = MatlabEngine.startMatlab();
                eng.eval("addpath('H:\\MatlabForJava\\imgs\\')");
                Object[] results = eng.feval(3, "getNDC", str);

                double n = (double) results[0];
                double d = (double) results[1];
                double c = (double) results[2];

                int in = (int) n;
                int id = (int) d;
                int ic = (int) c;
                // 5. 构建label
                String label = result + ": " + in + "*" + id + "*" + ic;
                datasets.setLabel(label);
                datasets.setValue(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 5 写到数据库中去
            datasetsService.save(datasets);
            // file.getInputStream() 可以用于读取文件内容
            // markedFile 是单选框选中的值
        }
        return R.success("数据上传成功");
    }

}
