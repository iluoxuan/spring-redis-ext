package com.mljr.spring.redis.ext.lock;

import com.mljr.spring.redis.ext.annotation.DistributeLock;

/**
 * Created by junqing.li on 17/3/4.
 *
 * 分布式锁
 */
public interface Lock {

	boolean tryLock(String key);

	void unlock();

	void init(DistributeLock distributeLock);
}