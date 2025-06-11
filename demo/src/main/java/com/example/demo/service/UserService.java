package com.example.demo.service;


import com.example.demo.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    Object login(String username, String password);

    Object register(User user);

    List<User> getAllUserInfo();

    boolean updateUserInfo(User user);

    boolean deleteUser(String username);

    boolean  updateUserImage(MultipartFile file, String filename);

    byte[] getUserImage(String fileName) throws IOException;

    List<User> getUserInfo(User user);
}
