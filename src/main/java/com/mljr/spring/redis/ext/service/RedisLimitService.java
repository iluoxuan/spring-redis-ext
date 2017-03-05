package com.mljr.spring.redis.ext.service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.mljr.spring.redis.ext.utils.Utils;

/**
 * Created by junqing.li on 16/1/7.
 * redis 限制业务
 */
public class RedisLimitService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 超过限制
	 * 
	 * @param key
	 * @param maxCnt
	 * @return
	 */
	public boolean notOverCount(String key, long maxCnt) {

		BoundValueOperations operations = stringRedisTemplate.boundValueOps(key);

		String cntStr = (String) operations.get();

		return Utils.isEmpty(cntStr) || Long.valueOf(cntStr) < maxCnt;
	}

	/**
	 * 是否标记
	 * 
	 * @param key
	 * @return
	 */
	public boolean isMarked(String key) {

		return notOverCount(key, 1);
	}

	/**
	 * 标记
	 * 
	 * @param key
	 * @param seconds
	 */
	public void mark(String key, long seconds) {

		incrCount(key, seconds);
	}

	/**
	 * 添加 次数
	 *
	 * @param key
	 * @param seconds
	 */
	public void incrCount(String key, long seconds) {

		BoundValueOperations operations = stringRedisTemplate.boundValueOps(key);

		long cnt = operations.increment(1);

		Long existExpire = operations.getExpire();

		// 不存在设置超时时间
		if (Objects.isNull(existExpire) || existExpire <= 0) {

			operations.expire(seconds, TimeUnit.SECONDS);
		}
	}

}
