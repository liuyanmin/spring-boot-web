package com.lym.springboot.web;

import com.lym.springboot.web.config.properties.SpringBootWebProperties;
import com.lym.springboot.web.util.PrintApplicationInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName Application
 * @Description 启动类
 * @Author LYM
 * @Date 2019/7/10 11:50
 * @Version 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.lym.springboot.web")
@EnableTransactionManagement
@EnableConfigurationProperties({SpringBootWebProperties.class})
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        // 打印项目信息
        PrintApplicationInfo.print(context);
    }

}
