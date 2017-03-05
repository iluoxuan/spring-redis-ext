package com.mljr.spring.redis.ext.lock;

import java.lang.reflect.Method;
import java.util.Objects;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import com.mljr.spring.redis.ext.annotation.DistributeLock;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/3/4.
 *
 * lock注解切点匹配 静态匹配不需要运行时动态匹配
 */
@Slf4j
public class LockAnnotationAdvisor extends StaticMethodMatcherPointcutAdvisor {

	@Override
	public boolean matches(Method method, Class<?> aClass) {

		DistributeLock distributeLock = AnnotationUtils.findAnnotation(method, DistributeLock.class);

		if (Objects.nonNull(distributeLock)) {

			log.info("[matches] method={}", method);

			return true;
		}

		return false;
	}
}
