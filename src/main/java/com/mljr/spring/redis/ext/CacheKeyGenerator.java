package com.mljr.spring.redis.ext;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * Created by junqing.li on 16/5/12.
 */
public class CacheKeyGenerator implements KeyGenerator {

	/**
	 * Generate a key based on the specified parameters.
	 */
	public static Object generateKey(Object... params) {

		if (params.length == 0) {

			return "";
		}
		if (params.length == 1) {
			Object param = params[0];
			if (param != null && !param.getClass().isArray()) {
				return param;
			}
		}


		for (Object param : params) {

			if (param != null && !Number.class.isAssignableFrom(param.getClass()) && !(param instanceof String)) {

				return ObjectUtils.nullSafeHashCode(params);
			}
		}

		return ObjectUtils.nullSafeToString(params);
	}

	/**
	 * Generate a key for the given method and its parameters.
	 *
	 * @param target
	 *            the target instance
	 * @param method
	 *            the method being called
	 * @param params
	 *            the method parameters (with any var-args expanded)
	 * @return a generated key
	 */
	@Override
	public Object generate(Object target, Method method, Object... params) {

		return params == null || params.length == 0 ? method.getName() : method.getName() + "_" + generateKey(params);

	}
}
