package com.lym.springboot.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by liuyanmin on 2019/10/17.
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    /**
     * 主机IP
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 密码
     */
    private String password;

    /**
     * 实例
     */
    private String db;

}
