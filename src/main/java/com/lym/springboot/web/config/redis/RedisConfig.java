package com.lym.springboot.web.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lym.springboot.web.config.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liuyanmin on 2019/9/29.
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    /**
     * redis
     * @return
     */
    @Bean(value = "redisTemplate")
    public RedisTemplate redisTemplate() {
        RedisTemplate template = new RedisTemplate(redisProperties.getHost(),
                redisProperties.getPort(), redisProperties.getPassword(), redisProperties.getDb());
        return template;
    }

    @Bean(value = "redisService")
    public RedisService redisService() {
        RedisService redisService = new RedisService(redisTemplate(), new ObjectMapper());
        return redisService;
    }

}
