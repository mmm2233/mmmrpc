package com.mmm.mmmrpc.serializer;

import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.HessianInput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/*
 * HessianSerializer
 */
public class HessianSerializer implements Serializer{
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HessianOutput output = new HessianOutput(baos);
        output.writeObject(obj);
        return baos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        HessianInput input = new HessianInput(new ByteArrayInputStream(data));
        T res = (T) input.readObject(clazz);
        return res;
    }
}
