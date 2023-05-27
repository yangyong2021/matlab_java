package cn.edu.swjtu.yangyong;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Yangyong
 * 2023-05-27 10:23
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan  // 扫描过滤器
@EnableTransactionManagement
public class MatlabJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatlabJavaApplication.class,args);
        log.info("项目启动成功");
    }
}
