package com.mmm.mmmrpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
 * Kryo 序列化工具类
 */
public class KryoSerializer implements Serializer {

    // Kryo线程不安全，使用ThreadLocal保存线程安全
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial
            (()->{
                Kryo kryo = new Kryo();
                kryo.setRegistrationRequired(false);
                return kryo;
            });
    @Override
    public <T> byte[] serialize(T obj) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        KRYO_THREAD_LOCAL.get().writeObject(output, obj);
        output.close();
        return baos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Input input = new Input(bais);
        T res = KRYO_THREAD_LOCAL.get().readObject(input, clazz);
        input.close();
        return res;
    }
}
