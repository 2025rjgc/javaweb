package com.example.demo.entity;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Data
public class Post {
  private Integer id; // 帖子id
  private Integer circleId; // 圈子id
  private String title; // 帖子标题
  private String bookName;  // 书籍名称
  private String username; // 用户名
  private String avatar; // 用户头像
  private String content; // 帖子内容
  private LocalDateTime postTime; // 发布时间
  private List<Comments> comments=new ArrayList<>(); // 评论列表
}
