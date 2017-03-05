package com.mljr.spring.redis.ext.config;

import java.lang.annotation.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mljr.spring.redis.ext.lock.DistributeLockConfig;

/**
 * 启用SpringRedisExt
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@Import({ RedisExtConfiguration.class, DistributeLockConfig.class })
@Configuration
public @interface EnableSpringRedisExt {

}