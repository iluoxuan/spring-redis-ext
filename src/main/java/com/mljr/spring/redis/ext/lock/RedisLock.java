package com.mljr.spring.redis.ext.lock;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.mljr.spring.redis.ext.utils.Utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by junqing.li on 17/3/4.
 */
@Getter
@Setter
public class RedisLock extends AbstractLock {

	private StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean tryLock(String key, TimeUnit unit) {
		return false;
	}

	@Override
	protected boolean tryLockInternal(String lockName, long expireTime) {

		BoundValueOperations stringOperations = stringRedisTemplate.boundValueOps(lockName);

		return stringOperations.setIfAbsent(String.valueOf(expireTime));
	}

	@Override
	protected long getSet(String lockName, long expireTime) {
		BoundValueOperations<String, String> stringOperations = stringRedisTemplate.boundValueOps(lockName);
		String value = stringOperations.getAndSet(String.valueOf(expireTime));

		if (Utils.isEmpty(value)) {
			return 0;
		}
		return Long.parseLong(value);
	}

	@Override
	public long getLockExpireTime(String lockName) {

		BoundValueOperations<String, String> stringOperations = stringRedisTemplate.boundValueOps(lockName);
		String value = stringOperations.get();
		if (Utils.isEmpty(value)) {

			return 0;
		}
		return Long.parseLong(value);
	}

	@Override
	protected void unlockInternal(Holder holder) {

		if (System.currentTimeMillis() < holder.getExpireTime()) {
			stringRedisTemplate.delete(holder.getLockKey());
		}

	}
}
