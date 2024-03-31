package com.mmm.example.common.service;

import com.mmm.example.common.model.User;

public interface UserService {
    User getUser(User user);

    default int getNumber() {
        return 1;
    }
}
