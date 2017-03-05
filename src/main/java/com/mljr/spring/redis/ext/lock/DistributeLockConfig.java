package com.mljr.spring.redis.ext.lock;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by junqing.li on 17/3/4.
 */
@Configurable
public class DistributeLockConfig {

	@Value("${redis.cache.namespace}")
	private String cacheNameSpace;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}

	@Bean
	public Lock lock() {
		RedisLock lock = new RedisLock();
		lock.setNamespace(cacheNameSpace);
		lock.setStringRedisTemplate(stringRedisTemplate);
		return lock;
	}

	@Bean
	public LockMethodInterceptor lockMethodInterceptor() {

		return new LockMethodInterceptor(lock());
	}

	@Bean
	public LockAnnotationAdvisor lockAnnotationAdvisor() {

		LockAnnotationAdvisor lockAnnotationAdvisor = new LockAnnotationAdvisor();
		lockAnnotationAdvisor.setAdvice(lockMethodInterceptor());

		return lockAnnotationAdvisor;
	}

}
