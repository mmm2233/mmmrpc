package com.mmm.example;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.mmm.example.common.model.User;
import com.mmm.example.common.service.UserService;
import com.mmm.mmmrpc.model.RpcRequest;
import com.mmm.mmmrpc.model.RpcResponse;
import com.mmm.mmmrpc.serializer.JdkSerializer;
import com.mmm.mmmrpc.serializer.Serializer;

public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        Serializer serializer = new JdkSerializer();
        RpcRequest request = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user}).build();

        try {
            byte[] bodyBytes = serializer.serialize(request);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        // TODO: send the data to remote server and wait for response
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
