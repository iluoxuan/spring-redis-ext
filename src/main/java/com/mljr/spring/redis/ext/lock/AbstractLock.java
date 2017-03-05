package com.mljr.spring.redis.ext.lock;

import java.util.Objects;

import com.mljr.spring.redis.ext.annotation.DistributeLock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/3/4.
 * 父类
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractLock implements Lock {

	// 线程变量
	private final ThreadLocal<Holder> LOCAL = new ThreadLocal<Holder>();

	/**
	 * 锁的过期时间(解决死锁)，单位：毫秒
	 */
	protected long lock_timeout = 3 * 1000;

	/**
	 * 尝试获取锁的间隔时间
	 */
	protected long retry_lock_time = 500;

	/**
	 * 尝试获取锁的次数
	 */
	protected int retry_times = 3;

	protected int ADD_MILLI_MASK = 1023;

	protected String namespace;

	@Override
	public void init(DistributeLock distributeLock) {

		this.lock_timeout = distributeLock.timeout();
		this.retry_lock_time = distributeLock.retryTime();
		this.retry_times = distributeLock.retryNum();
	}

	@Override
	public boolean tryLock(String lockName) {

		lockName = namespace.concat("-") + lockName;

		// get lock
		boolean lock = false;
		// retry times
		int retry = 1;

		long expireTime = 0;

		while (!lock) {
			expireTime = System.currentTimeMillis() + lock_timeout + 1;
			LOCAL.set(new Holder(lockName, expireTime));
			lock = tryLockInternal(lockName, expireTime);
			if (lock || (System.currentTimeMillis() > getLockExpireTime(lockName)
					&& System.currentTimeMillis() > getSet(lockName, expireTime))) {
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

	protected abstract boolean tryLockInternal(String lockName, long expireTime);

	/**
	 * 设置锁的过期时间戳并返回设置前的过期时间戳
	 *
	 * @param lockName
	 * @return
	 */
	protected abstract long getSet(String lockName, long expireTime);

	/**
	 * 获取锁的过期时间戳
	 *
	 * @param lockName
	 * @return
	 */
	public abstract long getLockExpireTime(String lockName);

	/**
	 * 释放锁
	 */
	@Override
	public void unlock() {

		// 为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，
		// 再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
		// 操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。
		Holder holder = LOCAL.get();
		if (Objects.isNull(holder)) {
			return;
		}

		unlockInternal(holder);

		LOCAL.remove();

	}

	protected abstract void unlockInternal(Holder holder);

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Holder {

		private String lockKey;

		private long expireTime;
	}
}
