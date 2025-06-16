package com.example.demo.entity;

import lombok.Data;

@Data
public class Circle {
    private Integer id;// 圈子id
    private String title; //  圈子名称
    private String owner; //  创建者
    private String cover; //  圈子封面
    private Integer members; //  成员数
    private Integer posts; //  帖子数
}
