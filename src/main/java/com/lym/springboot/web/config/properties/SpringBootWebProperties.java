package com.lym.springboot.web.config.properties;

import com.lym.springboot.web.common.constant.SpringBootWebInterceptorConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Created by liuyanmin on 2019/10/10.
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "spring-boot-web")
public class SpringBootWebProperties {

    @NestedConfigurationProperty
    private SpringBootWebInterceptorConfig interceptorConfig;


}
