package com.mljr.spring.redis.ext.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.mljr.spring.redis.ext.*;
import com.mljr.spring.redis.ext.service.RedisLimitService;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by iluoxuan on 16/10/16.
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
public class RedisExtConfiguration extends CachingConfigurerSupport {

	@Value("${redis.maxIdle}")
	private int maxIdle;

	@Value("${redis.maxTotal}")
	private int maxTotal;

	@Value("${redis.maxIdle}")
	private int minIdle;

	@Value("${redis.server.host}")
	private String redisHost;

	@Value("${redis.server.port}")
	private int redisPort;

	@Value("${redis.server.timeout}")
	private int redisTimeOut;

	@Value("${redis.server.pwd}")
	private String redisPwd;

	@Value("${redis.db}")
	private int redisDb;

	@Value("${redis.cache.namespace}")
	private String cacheNameSpace;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/**
	 * jedisPool 配置
	 *
	 * @return
	 */
	@Bean
	JedisPoolConfig jedisPoolConfig() {

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setTestOnBorrow(true);

		return jedisPoolConfig;
	}

	@Bean
	RedisConnectionFactory jedisConnectionFactory() {

		JedisConnectionFactory factory = new JedisConnectionFactory();

		factory.setHostName(redisHost);
		factory.setPort(redisPort);
		factory.setTimeout(redisTimeOut);
		factory.setPassword(redisPwd);
		factory.setUsePool(true);
		factory.setDatabase(redisDb);
		factory.setPoolConfig(jedisPoolConfig());
		return factory;
	}

	@Bean
	StringSerializer stringSerializer() {

		return new StringSerializer();
	}

	@Bean
	FastjsonSerializer fastjsonSerializer() {

		return new FastjsonSerializer();
	}

	@Bean
	RedisTemplate redisTemplate() {

		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setKeySerializer(stringSerializer());
		redisTemplate.setHashKeySerializer(stringSerializer());
		redisTemplate.setValueSerializer(fastjsonSerializer());
		redisTemplate.setHashValueSerializer(fastjsonSerializer());

		redisTemplate.setConnectionFactory(jedisConnectionFactory());

		return redisTemplate;
	}

	@Bean
	StringRedisTemplate stringRedisTemplate() {

		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(jedisConnectionFactory());

		return stringRedisTemplate;
	}

	@Bean
	NameSpaceCachePrefix nameSpaceCachePrefix() {
		NameSpaceCachePrefix nameSpaceCachePrefix = new NameSpaceCachePrefix();

		nameSpaceCachePrefix.setNameSpace(cacheNameSpace);

		return nameSpaceCachePrefix;
	}

	/**
	 * 多级缓存
	 * 
	 * @return
	 */
	@Bean
	@Override
	public CacheManager cacheManager() {

		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
		redisCacheManager.setCachePrefix(nameSpaceCachePrefix());
		redisCacheManager.setUsePrefix(true);

		ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();

		CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
		compositeCacheManager.setCacheManagers(Arrays.asList(redisCacheManager));
		compositeCacheManager.setFallbackToNoOpCache(true);

		return compositeCacheManager;
	}

	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return new CacheKeyGenerator();
	}

	@Bean
	RedisDistributedLock redisDistributedLock() {

		RedisDistributedLock redisDistributedLock = new RedisDistributedLock();
		redisDistributedLock.setStringRedisTemplate(stringRedisTemplate());

		return redisDistributedLock;

	}

	@Override
	public CacheResolver cacheResolver() {

		return new SimpleCacheResolver(cacheManager());
	}

	@Bean
	public RedisLimitService redisLimitService() {

		return new RedisLimitService();
	}
}
