package com.mljr.spring.redis.ext;

import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.BasicOperation;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CachePutOperation;

import java.util.Collection;

/**
 * Created by iluoxuan on 16/10/24.
 */
public class ExpireCacheResolver extends AbstractCacheResolver {

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {

        BasicOperation BasicOperation = context.getOperation();

        if (BasicOperation instanceof CachePutOperation) {

            CachePutOperation cachePutOperation = (CachePutOperation) BasicOperation;

            String unless = cachePutOperation.getUnless();


        }


        return context.getOperation().getCacheNames();
    }
}
