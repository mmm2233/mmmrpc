package com.mmm.example.provider;

import com.mmm.example.common.model.User;
import com.mmm.example.common.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.print("UserServiceImpl.getUser()");
        System.out.println(user.getName());
        return user;
    }
}
