package com.lym.springboot.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Jwt 配置属性
 * Created by liuyanmin on 2019/10/16.
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring-boot-web.jwt")
public class JwtProperties {

    /**
     * 秘钥
     */
    private String secret;

    /**
     * 签发人
     */
    private String issuer;

    /**
     * 主题
     */
    private String subject;

    /**
     * 签发目标
     */
    private String audience;

}
