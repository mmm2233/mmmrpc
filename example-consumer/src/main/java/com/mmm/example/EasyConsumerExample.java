package com.mmm.example;

import com.mmm.example.common.model.User;
import com.mmm.example.common.service.UserService;
import com.mmm.mmmrpc.proxy.ServiceProxyFactory;

public class EasyConsumerExample {
    public static void main(String[] args) {
        UserService userService = null;
        User user = new User();
        user.setName("test");

        // 调用
        //User userServiceUser = userService.getUser(user);
        //静态代理
        userService = new UserServiceProxy();

        //userService = ServiceProxyFactory.getProxy(UserService.class);
        if (userService != null) {
            User userServiceUser = userService.getUser(user);
            System.out.println(userService.getUser(user));
        }else {
            System.out.println("userServiceUser is null");
        }
    }
}
