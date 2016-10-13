package com.mljr.spring.redis.ext;

import com.mljr.spring.redis.ext.utils.Utils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Created by junqing.li on 16/5/5.
 * key string序列化
 */
public class StringSerializer implements RedisSerializer<Object> {

    private final Charset charset = Charset.forName("UTF8");

    @Override
    public byte[] serialize(Object object) throws SerializationException {

        if (Objects.isNull(object)) {

            return Utils.EMPTY_ARRAY;
        }

        if (Number.class.isAssignableFrom(object.getClass())) {

            return String.valueOf(object).getBytes(charset);
        }

        if (object instanceof String) {

            return ((String) object).getBytes(charset);
        }


        throw new SerializationException("Could not support this class type ={}" + object.getClass());

    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {

        if (Utils.isEmpty(bytes)) {

            return Utils.EMPTY_ARRAY;
        }

        return new String(bytes, charset);

    }
}
