package com.mljr.spring.redis.ext.config;

import com.mljr.spring.redis.ext.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by iluoxuan on 16/10/16.
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
@PropertySource("classpath:/redis.properties")
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

    @Bean
    @Override
    public CacheManager cacheManager() {

        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());

        cacheManager.setCachePrefix(nameSpaceCachePrefix());
        cacheManager.setUsePrefix(true);

        return cacheManager;
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

}
