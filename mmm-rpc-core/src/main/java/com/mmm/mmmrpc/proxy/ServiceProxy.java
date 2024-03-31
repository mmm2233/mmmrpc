package com.mmm.mmmrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.mmm.example.common.model.User;
import com.mmm.mmmrpc.model.RpcRequest;
import com.mmm.mmmrpc.model.RpcResponse;
import com.mmm.mmmrpc.serializer.JdkSerializer;
import com.mmm.mmmrpc.serializer.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 创建序列化器对象
        Serializer serializer = new JdkSerializer();

        // 构建RpcRequest对象
        RpcRequest request = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName()) // 设置服务名
                .methodName(method.getName()) // 设置方法名
                .parameterTypes(method.getParameterTypes()) // 设置参数类型
                .args(args).build(); // 设置参数值

        try {
            // 将RpcRequest对象序列化为字节数组
            byte[] bodyBytes = serializer.serialize(request);
            byte[] result;

            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                // 发送HTTP POST请求并获取响应的字节数组
                result = httpResponse.bodyBytes();
            }

            // 将响应的字节数组反序列化为RpcResponse对象
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);

            // 返回RpcResponse对象中的数据
            return rpcResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回null表示发生异常
        return null;
    }

}
