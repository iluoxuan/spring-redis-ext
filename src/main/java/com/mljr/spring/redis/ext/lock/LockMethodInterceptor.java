package com.mljr.spring.redis.ext.lock;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;

import com.mljr.spring.redis.ext.annotation.DistributeLock;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/3/4.
 */
@Slf4j
@Setter
@Getter
public class LockMethodInterceptor implements MethodInterceptor {

	private Lock lock;

	private DistributeLockExpressionEvaluator evaluator = new DistributeLockExpressionEvaluator();

	public LockMethodInterceptor() {
	}

	public LockMethodInterceptor(Lock lock) {
		this.lock = lock;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		DistributeLock distributeLock = AnnotationUtils.findAnnotation(invocation.getMethod(), DistributeLock.class);

		lock.init(distributeLock);

		String lockKey = getLockKey(distributeLock, invocation);

		if (!lock.tryLock(lockKey)) {

			throw new DistributeLockException(distributeLock.errMsg() + " lockkey=" + lockKey);
		}

		try {

			return invocation.proceed();

		} catch (Throwable throwable) {

			throw throwable;

		} finally {

			lock.unlock();
		}

	}

	private String getLockKey(DistributeLock distributeLock, MethodInvocation invocation) {

		Assert.hasText(distributeLock.key(), "distribute key cannot empty");
		Assert.hasText(distributeLock.action(), "distribute action cannot empty");

		EvaluationContext evaluationContext = evaluator.createEvaluationContext(invocation.getMethod(), invocation.getArguments(),
				invocation.getThis(), invocation.getThis().getClass());

		String key = (String) evaluator.key(distributeLock.key(),
				new AnnotatedElementKey(invocation.getMethod(), invocation.getThis().getClass()), evaluationContext);

		key = distributeLock.action().concat(":") + key;

		log.info("[getLockKey] el get key={}", key);

		return key;

	}
}
