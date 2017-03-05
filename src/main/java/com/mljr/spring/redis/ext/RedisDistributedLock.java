package com.mljr.spring.redis.ext;

import java.util.Objects;

import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.mljr.spring.redis.ext.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis实现分布式锁
 * <p>
 * Created by junqing.li on 16/6/5.
 */
@Slf4j
@Getter
@Setter
public class RedisDistributedLock {

	private StringRedisTemplate stringRedisTemplate;

	// 线程变量
	private final ThreadLocal<Holder> LOCAL = new ThreadLocal<Holder>();

	/**
	 * 锁的过期时间(解决死锁)，单位：毫秒
	 */
	private long lock_timeout = 3 * 1000;

	/**
	 * 尝试获取锁的间隔时间
	 */
	private long retry_lock_time = 500;

	/**
	 * 尝试获取锁的次数
	 */
	private int retry_times = 3;

	private int ADD_MILLI_MASK = 1023;

	/**
	 * 锁的命名空间
	 */
	private String namespace;

	/**
	 * try lock
	 *
	 * @param lockName
	 * @return
	 */
	public boolean tryLock(String lockName) {

		lockName = namespace + lockName;

		// get lock
		boolean lock = false;
		// retry times
		int retry = 1;

		long expireTime = 0;

		BoundValueOperations stringOperations = stringRedisTemplate.boundValueOps(lockName);
		while (!lock) {
			expireTime = System.currentTimeMillis() + lock_timeout + 1;
			LOCAL.set(new Holder(lockName, expireTime));
			lock = stringOperations.setIfAbsent(String.valueOf(expireTime));
			if (lock || (System.currentTimeMillis() > get(lockName) && System.currentTimeMillis() > getSet(lockName, expireTime))) {
				break;
			} else {
				if (++retry > retry_times) {

					log.info("[tryLock] trylock  lockname ={}", lockName);

					return false;
				}
				try {
					Thread.sleep(retry_lock_time * System.currentTimeMillis() & ADD_MILLI_MASK);
				} catch (InterruptedException e) {

				}
			}
		}

		return true;

	}

	/**
	 * 释放锁
	 */
	public void unlock() {

		// 为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。
		Holder holder = LOCAL.get();
		if (Objects.isNull(holder)) {
			return;
		}
		if (System.currentTimeMillis() < holder.getExpireTime()) {
			stringRedisTemplate.delete(holder.getLockKey());
		}

		LOCAL.remove();

	}

	/**
	 * 获取锁的过期时间戳
	 *
	 * @param lockName
	 * @return
	 */
	private long get(String lockName) {
		BoundValueOperations<String, String> stringOperations = stringRedisTemplate.boundValueOps(lockName);
		String value = stringOperations.get();
		if (Utils.isEmpty(value)) {

			return 0;
		}
		return Long.parseLong(value);
	}

	/**
	 * 设置锁的过期时间戳并返回设置前的过期时间戳
	 *
	 * @param lockName
	 * @param timestamp
	 * @return
	 */
	private long getSet(String lockName, long timestamp) {
		BoundValueOperations<String, String> stringOperations = stringRedisTemplate.boundValueOps(lockName);
		String value = stringOperations.getAndSet(String.valueOf(timestamp));

		if (Utils.isEmpty(value)) {
			return 0;
		}
		return Long.parseLong(value);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Holder {

		private String lockKey;

		private long expireTime;
	}

}
