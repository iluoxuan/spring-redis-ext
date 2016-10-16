package com.mljr.spring.redis.ext.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用SpringRedisExt
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({RedisExtConfiguration.class})
@Configuration
public @interface EnableSpringRedisExt {

}