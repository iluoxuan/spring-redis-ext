package com.mljr.spring.redis.ext.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁 注解
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributeLock {

	/**
	 * 锁的key
	 */
	String key() default "";

	/**
	 * 锁的动作
	 */
	String action() default "";

	/**
	 * 获取锁 失败的提示
	 */
	String errMsg() default "get lock fail";

	/**
	 * 锁的过期时间 ms毫秒
	 * 
	 * @return
	 */
	long timeout() default 3 * 1000;

	/**
	 * 重试次数
	 * 
	 * @return
	 */
	int retryNum() default 3;

	/**
	 * 获取锁的间隔时间 ms
	 * 
	 * @return
	 */
	int retryTime() default 500;

}