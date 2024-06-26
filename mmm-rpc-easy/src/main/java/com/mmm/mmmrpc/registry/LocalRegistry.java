package com.mmm.mmmrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * 本地注册中心
 */
public class LocalRegistry {
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    public static void register(String serviceName, Class<?> clazz) {
        map.put(serviceName, clazz);
    }

    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    public static void remove(String serviceName) {
        map.remove(serviceName);
    }
}
