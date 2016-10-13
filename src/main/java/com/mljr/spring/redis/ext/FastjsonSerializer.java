package com.mljr.spring.redis.ext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mljr.spring.redis.ext.utils.Utils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Created by junqing.li on 16/5/5.
 * <p>
 * 扩展spring redis fastjson serializer
 * 解决不了 类型问题啊
 * <p>
 * 解决:
 * SerializerFeature.WriteClassName 在json序列化加入 @type字段
 */
public class FastjsonSerializer implements RedisSerializer<Object> {

    private SerializerFeature feature = SerializerFeature.WriteClassName;

    private final Charset charset = Charset.forName("UTF8");

    @Override
    public byte[] serialize(Object t) throws SerializationException {

        if (Objects.isNull(t)) {
            return Utils.EMPTY_ARRAY;
        }
        try {

            return JSON.toJSONString(t, feature).getBytes(charset);

        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }

    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {

        if (Utils.isEmpty(bytes)) {
            return null;
        }
        try {

            String jsonStr = new String(bytes, charset);

            return JSON.parseObject(jsonStr, Object.class);

        } catch (Exception ex) {

            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

}
