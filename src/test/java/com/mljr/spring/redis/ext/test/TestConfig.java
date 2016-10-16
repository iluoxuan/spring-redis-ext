package com.mljr.spring.redis.ext.test;

import com.mljr.spring.redis.ext.config.EnableSpringRedisExt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by iluoxuan on 16/10/12.
 */
@Configuration
@EnableSpringRedisExt
public class TestConfig {

    @Bean
    CacheService cacheService() {
        return new CacheService();
    }
}
