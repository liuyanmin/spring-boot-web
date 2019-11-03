package com.lym.springboot.web.util;

import lombok.extern.log4j.Log4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Created by liuyanmin on 2019/10/9.
 */
@Log4j
public class PrintApplicationInfo {

    /**
     * 启动成功之后，打印项目信息
     */
    public static void print(ConfigurableApplicationContext context){
        ConfigurableEnvironment environment = context.getEnvironment();

        // 项目名称
        String projectFinalName = environment.getProperty("info.project-name");
        // 项目版本
        String projectVersion = environment.getProperty("info.project-version");
        // 项目profile
        String profileActive = environment.getProperty("spring.profiles.active");
        // 项目路径
        String contextPath = environment.getProperty("server.servlet.context-path");
        // 项目端口
        String port = environment.getProperty("server.port");

        log.info("projectName : " + projectFinalName);
        log.info("projectVersion : " + projectVersion);
        log.info("profileActive : " + profileActive);
        log.info("contextPath : " + contextPath);
        log.info("port : " + port);

        String homeUrl = "http://" + IpUtil.getLocalhostIp() + ":" + port + contextPath;
        String swaggerUrl = "http://" + IpUtil.getLocalhostIp() + ":" + port + "/swagger-ui.html";
        String druidUrl = "http://" + IpUtil.getLocalhostIp() + ":" + port + "/druid/login.html";

        log.info("home: " + homeUrl);
        log.info("docs: " + swaggerUrl);
        log.info("druid: " + druidUrl);
        log.info("spring-boot-web project start success...........");
    }
}
