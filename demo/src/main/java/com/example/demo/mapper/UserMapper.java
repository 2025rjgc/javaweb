package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    // 根据用户名和密码查询用户
    List<User> findByUsernameAndPassword(@Param("userName") String userName, @Param("password") String password);

    // 多条件查询用户
    List<User> find(User user);

    // 添加用户
    void insert(User user);

    // 查询所有用户
    List<User> findAll();

    // 分页查询用户
    List<User> findAllByPage(@Param("start") int start, @Param("pageSize") int pageSize);

    // 修改用户信息
    int updateUserInfo(@Param("user") User user);

    // 删除用户
    int deleteUser(@Param("username") String username);

    // 根据id查询用户
    User findById(@Param("userId") Integer userId);

    // 根据用户名查询用户
    User findByName(@Param("username") String owner);
}