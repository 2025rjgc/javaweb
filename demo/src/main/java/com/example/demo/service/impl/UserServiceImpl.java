package com.example.demo.service.impl;

import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    // 构造器注入
    private final UserMapper userMapper;
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    // 登录
    @Override
    public Object login(String username, String password) {
        List<User> userData = userMapper.findByUsernameAndPassword(username, password);
        // 查询用户是否存在
        if (!userData.isEmpty()){
            HashMap<String, Object> map = new HashMap<>();
            // 生成JWT令牌
            String JWT_token = JWTUtils.createJWT(username, password, 1000 * 60 * 60 * 24 * 7);
            map.put("userID", userData.get(0).getUserId());
            map.put("username", userData.get(0).getUsername());
            map.put("role", userData.get(0).getRole());
            map.put("token", JWT_token);
            return map;
        }
        return null;
    }

    // 注册
    @Override
    public Object register(User user) {
        if (userMapper.find(user).isEmpty()) {
            if(user.getUsername() == null || user.getPassword() == null) {
                System.out.println("用户名或密码为空");
                return null;
            }
            // 插入用户
            userMapper.insert(user);
            HashMap<String, Object> map = new HashMap<>();
            map.put("ID_num", user.getID_num());
            map.put("username", user.getUsername());
            return map;
        }
        System.out.println("用户已存在");
        return null;
    }

    // 获取用户列表
    @Override
    public List<User> getAllUserInfo() {
        return userMapper.findAll();
    }

    // 修改用户信息
    @Override
    public boolean updateUserInfo(User user) {
        return userMapper.updateUserInfo(user.getUsername(), user) > 0;
    }

    // 删除用户
    @Override
    public boolean deleteUser(String username) {
        return userMapper.deleteUser(username) > 0;
    }

    @Override
    public List<User> getUserInfo(User user) {
        return userMapper.find(user);
    }
}
