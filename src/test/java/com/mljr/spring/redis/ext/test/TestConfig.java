package com.mljr.spring.redis.ext.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.mljr.spring.redis.ext.config.EnableSpringRedisExt;

/**
 * Created by iluoxuan on 16/10/12.
 */
@Configuration
@EnableSpringRedisExt
@PropertySource("classpath:/redis.properties")
//@ImportResource("classpath:/spring-redis-ext.xml")
public class TestConfig {

	@Bean
	CacheService cacheService() {
		return new CacheService();
	}

	@Bean
	LockService lockService() {
		return new LockService();
	}
}
