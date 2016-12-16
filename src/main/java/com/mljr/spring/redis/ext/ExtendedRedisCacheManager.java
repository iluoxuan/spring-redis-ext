package com.mljr.spring.redis.ext;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.core.RedisOperations;

import com.mljr.spring.redis.ext.utils.Utils;

import lombok.extern.slf4j.Slf4j;

/**
 * redisCacheManager扩展
 */
@Slf4j
public class ExtendedRedisCacheManager extends RedisCacheManager {

	private static final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");

	private static final Pattern pattern = Pattern.compile("[+\\-*/%]");

	private String defaultCacheName;

	private char separator = '#';

	public ExtendedRedisCacheManager(RedisOperations redisOperations) {
		this(redisOperations, Collections.<String> emptyList());
	}

	public ExtendedRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
		super(redisOperations, cacheNames);
	}

	@Override
	public Cache getCache(String name) {
		// try to get cache by name
		RedisCache cache = (RedisCache) super.getCache(name);
		if (Objects.nonNull(cache)) {
			return cache;
		}

		// there's no cache which has given name
		// find separator in cache name
		int index = name.lastIndexOf(getSeparator());
		if (index < 0) {
			return super.getCache(name);
		}

		// split name by the separator
		String cacheName = name.substring(0, index);
		if (Utils.isEmpty(cacheName)) {
			cacheName = defaultCacheName;
		}
		cache = (RedisCache) super.getCache(cacheName);
		if (Objects.isNull(cache)) {
			return null;
		}

		// get expiration from name
		Long expiration = getExpiration(name, index);

		if (Objects.isNull(expiration) || expiration < 0) {

			log.warn("Default expiration time will be used for cache '{}' because cannot parse '{}', cacheName : " + cacheName + ", name : "
					+ name);

			return cache;
		}

		return new RedisCache(cacheName, (isUsePrefix() ? getCachePrefix().prefix(cacheName) : null), getRedisOperations(), expiration);
	}

	public char getSeparator() {
		return separator;
	}

	/**
	 * Char that separates cache name and expiration time, default: #.
	 *
	 * @param separator
	 */
	public void setSeparator(char separator) {
		this.separator = separator;
	}

	private Long getExpiration(final String name, final int separatorIndex) {

		Long expiration = -1L;
		String expirationAsString = name.substring(separatorIndex + 1);
		try {
			// calculate expiration, support arithmetic expressions.
			if (pattern.matcher(expirationAsString).find()) {
				expiration = (long) Double.parseDouble(scriptEngine.eval(expirationAsString).toString());
			} else {
				expiration = Long.parseLong(expirationAsString);
			}
		} catch (NumberFormatException ex) {

			log.error("can not separate expiration time from cache={}", ex);

		} catch (ScriptException e) {

			log.error("can not separate expiration time from cache={}", e);
		}

		return expiration;
	}

	@Override
	public void setUsePrefix(boolean usePrefix) {
		super.setUsePrefix(usePrefix);
	}

	@Override
	public void setCachePrefix(RedisCachePrefix cachePrefix) {
		super.setCachePrefix(cachePrefix);
	}

	public void setDefaultCacheName(String defaultCacheName) {
		this.defaultCacheName = defaultCacheName;
	}
}