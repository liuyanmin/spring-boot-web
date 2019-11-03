package com.lym.springboot.web.config.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lym.springboot.web.common.constant.SpringBootWebInterceptorConfig;
import com.lym.springboot.web.config.properties.SpringBootWebProperties;
import com.lym.springboot.web.common.interceptor.JwtInterceptor;
import com.lym.springboot.web.common.interceptor.LogInterceptor;
import com.lym.springboot.web.common.interceptor.PermissionInterceptor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

/**
 * 拦截器注册类
 * Created by liuyanmin on 2019/9/30.
 */
@Log4j
@Configuration
public class IntercpetorConfig implements WebMvcConfigurer {

    @Autowired
    private SpringBootWebProperties springBootWebProperties;
    @Autowired
    private LogInterceptor logInterceptor;
    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Autowired
    private PermissionInterceptor permissionInterceptor;
    @Autowired
    private ObjectMapper objectMapper;


    @PostConstruct
    public void init() throws JsonProcessingException {
        log.info("SpringBootWebProperties: " + objectMapper.writeValueAsString(springBootWebProperties));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        SpringBootWebInterceptorConfig interceptorConfig = springBootWebProperties.getInterceptorConfig();

        // JWT 拦截器
        if (interceptorConfig.getJwtConfig().isEnabled()) {
            registry.addInterceptor(jwtInterceptor)
                    .addPathPatterns(interceptorConfig.getJwtConfig().getIncludePath())
                    .excludePathPatterns(interceptorConfig.getJwtConfig().getExcludePath());
        }

        // 日志拦截器
        if (interceptorConfig.getLogConfig().isEnabled()) {
            registry.addInterceptor(logInterceptor)
                    .addPathPatterns(interceptorConfig.getLogConfig().getIncludePath())
                    .excludePathPatterns(interceptorConfig.getLogConfig().getExcludePath());
        }

        // 权限拦截器
        if (interceptorConfig.getPermissionConfig().isEnabled()) {
            registry.addInterceptor(permissionInterceptor)
                    .addPathPatterns(interceptorConfig.getPermissionConfig().getIncludePath())
                    .excludePathPatterns(interceptorConfig.getPermissionConfig().getExcludePath());
        }
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 设置swagger静态资源访问
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
