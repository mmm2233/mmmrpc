package com.mmm.mmmrpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    @Override
    public void dostart(int port) {
        // 创建一个Vert.x实例
        Vertx vertx = Vertx.vertx();

        // 创建一个HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 设置请求处理器，使用HttpServerHandler类来处理请求
        //server.requestHandler(request -> {
        //    System.out.println("Received request:" + request.method() + " " + request.uri());
        //    request.response().putHeader("Content-Type", "text/plain").end("Hello from Vert.x HTTP server!");
        //});
        server.requestHandler(new HttpServerHandler());

        // 启动服务器，监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                // 如果启动成功，打印服务器启动信息
                System.out.println("Server started on port " + server.actualPort());
            } else {
                // 如果启动失败，打印错误信息并输出堆栈跟踪
                System.out.println("Failed to start server" + result.cause());
            }
        });
    }
}
