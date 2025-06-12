package com.example.demo.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;  // 用户id
    private Integer role;    // 用户权限(0-普通用户, 1-导师, 2-管理员',)
    private String username;    // 用户名
    private String password;    // 用户密码
    private String personId;  // 学工号
    private Integer circleId;    // 圈子id
    private String major;   // 专业
    private String avatar; // 用户头像
    private String email;   // 用户邮箱
    private String phone;   // 用户电话
    private String signature;    // 个性签名
    private LocalDateTime createTime;  // 用户创建时间
    private LocalDateTime updateTime;  // 用户信息更新时间
}
