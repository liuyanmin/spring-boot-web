package com.lym.springboot.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by liuyanmin on 2019/10/17.
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring-boot-web.login")
public class LoginProperties {

    /**
     * 是否支持多端登录
     */
    private Boolean multi;

    /**
     * 过期时间，单位：分钟
     */
    private Integer expireMinutes;

    /**
     * rsa 公钥
     */
    private String publicKey;

    /**
     * 登录密码解密私钥（rsa解密）
     */
    private String privateKey;

    /**
     * 新建用户初始密码
     */
    private String initPassword;
}
