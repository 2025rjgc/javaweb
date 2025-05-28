package com.example.demo.service;


import com.example.demo.pojo.User;

import java.util.List;

public interface UserService {

    Object login(String username, String password);

    Object register(User user);

    List<User> getAllUserInfo();

    boolean updateUserInfo(User user);

    boolean deleteUser(String username);

    List<User> getUserInfo(User user);
}
