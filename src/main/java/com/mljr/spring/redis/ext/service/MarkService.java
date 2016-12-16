package com.mljr.spring.redis.ext.service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.mljr.fenqi.crm.common.ext.SpringHelper;

/**
 * Created by junqing.li on 16/6/17.
 * <p>
 * 标记业务
 */
public class MarkService {

	// key
	private String key;

	// string oprations
	private BoundValueOperations<String, String> operations;

	// 标记上线
	private int limit;

	public MarkService(String key, int limit) {

		this.key = "fenqi_crm_" + key;
		this.limit = limit;
		this.operations = SpringHelper.getBean(StringRedisTemplate.class).boundValueOps(key);
	}

	/**
	 * 带超时时间
	 *
	 * @param key
	 * @param limit
	 * @param expire
	 */
	public MarkService(String key, int limit, int expire) {

		this(key, limit);
		Long existExpire = this.operations.getExpire();

		// 不存在设置超时时间
		if (Objects.isNull(existExpire) || existExpire <= 0) {

			this.operations.expire(expire, TimeUnit.SECONDS);
		}

	}

	/**
	 * 标记 一个值
	 *
	 * @return
	 */
	public long mark() {

		return operations.increment(1);
	}

	/**
	 * 重置
	 */
	public void reset() {

		operations.set("0");
	}

	/**
	 * 已经标记了
	 *
	 * @return
	 */
	public boolean isMarked() {

		int mark = StringUtils.isEmpty(operations.get()) ? 0 : Integer.valueOf(operations.get());

		return mark >= limit;
	}
}
