package com.mmm.mmmrpc.server;

import com.mmm.mmmrpc.model.RpcRequest;
import com.mmm.mmmrpc.model.RpcResponse;
import com.mmm.mmmrpc.registry.LocalRegistry;
import com.mmm.mmmrpc.serializer.JdkSerializer;
import com.mmm.mmmrpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        // 创建序列化对象
        final Serializer serializer = new JdkSerializer();
        // 打印接收到的请求方法和URI
        System.out.println("request received:" + request.method() + " " + request.uri());
        // 设置请求体处理器
        request.bodyHandler(body -> {
            // 将请求体转换为字节数组
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                // 将字节数组反序列化为RpcRequest对象
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 创建RpcResponse对象
            RpcResponse rpcResponse = new RpcResponse();
            // 如果RpcRequest为null，则设置RpcResponse的消息为"request is null!"，并发送响应
            if (rpcRequest != null){
                rpcResponse.setMessage("request is null!");
                doResponse(request, rpcResponse, serializer);
                return;
            }
            try {
                // 根据RpcRequest中的服务名从本地注册表中获取实现类
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                // 获取实现类中对应的方法
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                // 调用方法并获取结果
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                // 设置RpcResponse的数据为调用结果
                rpcResponse.setData(result);
                // 设置RpcResponse的数据类型为方法返回类型
                rpcResponse.setDataType(method.getReturnType());
                // 设置RpcResponse的消息为"ok"
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                // 设置RpcResponse的消息为异常信息
                rpcResponse.setMessage(e.getMessage());
                // 设置RpcResponse的异常为捕获的异常
                rpcResponse.setException(e);
            }
            // 发送RpcResponse响应
            doResponse(request, rpcResponse, serializer);
        });
    }



    void doResponse(HttpServerRequest request, RpcResponse rpcResponse,Serializer serializer){
        HttpServerResponse response = request.response().putHeader("Content-Type", "application/json");
        try {
            byte[] serialize = serializer.serialize(rpcResponse);
            response.end(Buffer.buffer(serialize));
        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
