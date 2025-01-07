package com.hgh.tingyue.service;

import com.hgh.tingyue.entity.User;

public interface UserService {
    User register(String username, String password, String email);

    String login(String username, String password);

    User getUserById(Long id);
}