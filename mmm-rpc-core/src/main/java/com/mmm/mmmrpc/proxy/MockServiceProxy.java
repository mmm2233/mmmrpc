package com.mmm.mmmrpc.proxy;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    Mock服务代理(JDK动态代理)
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    private static final Faker faker = new Faker();

    /*
        代理方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("MockServiceProxy.invoke() method={}", method.getName());
        return fillAllFields(returnType);
    }
    public Object fillAllFields(Class<?> clazz) {
        try {
            if (clazz.isPrimitive()) return getFakerValue(clazz);
            // 创建类的实例
            Object instance = clazz.getDeclaredConstructor().newInstance();

            // 获取类及其父类的所有字段
            Field[] fields = clazz.getDeclaredFields();
            Class<?> superClass = clazz.getSuperclass();
            while (superClass != null) {
                fields = combineFields(fields, superClass.getDeclaredFields());
                superClass = superClass.getSuperclass();
            }

            // 遍历所有字段并尝试使用Faker填充值
            for (Field field : fields) {
                field.setAccessible(true); // 设置可访问性，以便访问私有字段
                Object value = getFakerValue(field.getType());
                field.set(instance, value); // 设置字段的值
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Field[] combineFields(Field[] first, Field[] second) {
        Field[] combined = new Field[first.length + second.length];
        System.arraycopy(first, 0, combined, 0, first.length);
        System.arraycopy(second, 0, combined, first.length, second.length);
        return combined;
    }

    private Object getFakerValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == int.class || type == Integer.class) {
                return faker.number().numberBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else if (type == long.class || type == Long.class) {
                return faker.number().numberBetween(Long.MIN_VALUE, Long.MAX_VALUE);
            } else if (type == double.class || type == Double.class) {
                return 0;
            } else if (type == float.class || type == Float.class) {
                return 0f;
            } else if (type == boolean.class || type == Boolean.class) {
                return false;
            } else if (type == char.class || type == Character.class) {
                return faker.letterify("?");
            } else if (type == byte.class || type == Byte.class) {
                return  0;
            } else if (type == short.class || type == Short.class) {
                return faker.number().numberBetween(0, 100);
            }
        } else if (String.class.equals(type)) {
            return faker.name().username();
        } else if (Date.class.equals(type)) {
            return faker.date().between(new Date(1999, 1, 1), new Date());
        } else if (List.class.isAssignableFrom(type)) {
            // Assuming the list contains String elements
            int listSize = faker.number().numberBetween(3, 10);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < listSize; i++) {
                list.add(faker.name().username());
            }
            return list;
        } else {
            throw new UnsupportedOperationException("No faker value available for type: " + type.getName());
        }
        return null;
    }
    

}
