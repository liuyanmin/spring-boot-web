package com.lym.springboot.web.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by liuyanmin on 2019/10/20.
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring-boot-web.mail")
public class MailProperties {

    /**
     * 邮件协议
     */
    private String protocol;

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 发件邮箱
     */
    private String from;

    /**
     * 发件邮箱密码
     */
    private String fromPassword;

    /**
     * 发件人名称
     */
    private String fromName;

    /**
     * 消息类型
     */
    private String messageType;

}
