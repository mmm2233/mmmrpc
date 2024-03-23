package com.mmm.example.provider;

import com.mmm.example.common.service.UserService;
import com.mmm.mmmrpc.registry.LocalRegistry;
import com.mmm.mmmrpc.server.HttpServer;
import com.mmm.mmmrpc.server.VertxHttpServer;

public class EasyProviderExample {
    public static void main(String[] args) {

        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        HttpServer server = new VertxHttpServer();
        server.dostart(8080);
    }
}
