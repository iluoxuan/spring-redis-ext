package com.mljr.spring.redis.ext;

import com.mljr.spring.redis.ext.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by junqing.li on 16/5/9.
 */
public class NameSpaceCachePrefix implements RedisCachePrefix {

    @Getter
    @Setter
    private String nameSpace;

    private final RedisSerializer serializer = new StringRedisSerializer();
    private final String delimiter;

    public NameSpaceCachePrefix() {
        this(":");
    }

    public NameSpaceCachePrefix(String delimiter) {
        this.delimiter = delimiter;
    }


    public byte[] prefix(String cacheName) {

        String cacheKey = Utils.isEmpty(nameSpace) ? "" : nameSpace;

        String key = cacheName;

        if (!Utils.isEmpty(cacheKey)) {

            key = cacheKey + ":" + cacheName;
        }
        return serializer.serialize((delimiter != null ? key.concat(delimiter) : key.concat(":")));
    }


}
