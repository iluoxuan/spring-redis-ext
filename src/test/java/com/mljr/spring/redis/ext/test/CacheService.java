package com.mljr.spring.redis.ext.test;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by iluoxuan on 16/10/12.
 */
@Service
@CacheConfig(cacheNames = "test")
public class CacheService {

	@Cacheable(key = "#id")
	public String getById(int id) {

		return "hello xxsss test";
	}

	@CachePut(cacheNames = "userID",key = "#id")
	public String getSById(int id) {

		return "hello CHNAGE test";
	}

	@CachePut
	public String get() {

		return "xxxxCacheable";
	}

}
