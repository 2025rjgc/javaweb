package com.example.demo.controller;

import com.example.demo.pojo.Result;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 登录
    @PostMapping("/login")
    public Result login(String username, String password) {
        Object login_data = userService.login(username, password);
        System.out.println("登录：u:"+username+" p:"+password);
        if (login_data != null) {
            System.out.println("登录成功");
            return Result.success(login_data);
        } else {
            System.out.println("用户名或密码错误");
            return Result.error("用户名或密码错误");
        }
    }

    // 注册
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        System.out.println("注册：" + user);
        Object register_data = userService.register(user);
        if (register_data != null) {
            return Result.success(register_data);
        } else {
            return Result.error("注册失败");
        }
    }

    // 获取用户信息
    @PostMapping("/getUserInfo")
    public Result getAllUserInfo(@RequestBody User user) {
        System.out.println("获取用户信息：" + user);
        List<User> userList = userService.getUserInfo(user);
        if (userList != null) {
            return Result.success(userList);
        } else {
            return Result.error("用户不存在");
        }
    }

    // 修改用户信息
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody User user) {
        if (userService.updateUserInfo(user)) {
            System.out.println("修改用户信息：" + user);
            return Result.success();
        } else {
            System.out.println("修改用户信息：" + user);
            return Result.error("修改失败");
        }
    }

    // 删除用户
    @PostMapping("/deleteUser")
    public Result deleteUser(String username) {
        if (userService.deleteUser(username)) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }
}
