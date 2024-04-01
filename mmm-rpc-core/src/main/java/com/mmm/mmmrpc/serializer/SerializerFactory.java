package com.mmm.mmmrpc.serializer;

import com.mmm.mmmrpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/*
 * 序列化工厂
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

//    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>(){{
//        put(SerializerKeys.JDK, new JdkSerializer());
//        put(SerializerKeys.HESSIAN, new HessianSerializer());
//        put(SerializerKeys.KRYO, new KryoSerializer());
//        put(SerializerKeys.JSON, new JsonSerializer());
//    }};

    /*
     * 默认序列化器
     */
    //private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static Serializer getInstance(String key)
    {
        //return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
