package com.mmm.mmmrpc.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmm.mmmrpc.model.RpcRequest;
import com.mmm.mmmrpc.model.RpcResponse;

import java.io.IOException;

public class JsonSerializer implements Serializer{
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        return objectMapper.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        T obj = objectMapper.readValue(data, clazz);
        if (obj instanceof RpcRequest) {
            return handleRequest((RpcRequest)obj,clazz);
        }
        if (obj instanceof RpcResponse) {
            return handleResponse((RpcResponse)obj,clazz);
        }
        return obj;
    }

    // 由于Object 的原始对象会被擦除，导致反序列化时会被作为LinkedHashMap无法转换成原始对象，所以需要重新处理
    private <T> T handleRequest(RpcRequest request, Class<T> clazz) throws IOException {
        Class<?> parameterTypes[] = request.getParameterTypes();
        Object[] args = request.getArgs();

        // 循环处里每个参数的类型
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> newClazz = parameterTypes[i];
            if (!newClazz.isAssignableFrom(args[i].getClass())) {
                byte[] argsBytes = objectMapper.writeValueAsBytes(args[i]);
                args[i] = objectMapper.readValue(argsBytes, newClazz);
            }
        }

        return clazz.cast(request);
    }

    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        byte[] dataBytes = objectMapper.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(objectMapper.readValue(dataBytes, rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }
}
