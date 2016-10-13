package com.mljr.spring.redis.ext;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import java.util.Collection;

/**
 * Created by junqing.li on 16/8/12.
 * 扩展cacheResolver
 */
public class ExtCacheResolver implements CacheResolver {

	/**
	 * Return the cache(s) to use for the specified invocation.
	 *
	 * @param context
	 *            the context of the particular invocation
	 * @return the cache(s) to use (never {@code null})
	 */
	@Override
	public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {

		context.getOperation();

		return null;
	}
}
