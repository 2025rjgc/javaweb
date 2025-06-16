package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Comments {
    private Integer id;//评论id
    private Integer postId; //帖子id
    private String username; //用户名
    private String content; //评论内容
    private LocalDateTime commentTime; //评论时间
}
