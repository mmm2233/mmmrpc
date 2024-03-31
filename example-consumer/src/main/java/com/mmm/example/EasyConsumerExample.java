package com.mmm.example;

import com.mmm.example.common.model.User;
import com.mmm.example.common.service.UserService;
import com.mmm.mmmrpc.proxy.ServiceProxyFactory;

public class EasyConsumerExample {
    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);;

        User user = new User();
        user.setName("test");

        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("null");
        }
        long number = userService.getNumber();
        System.out.println(number);
        // 调用
        //User userServiceUser = userService.getUser(user);
        //静态代理
        //userService = new UserServiceProxy();
    }
}
