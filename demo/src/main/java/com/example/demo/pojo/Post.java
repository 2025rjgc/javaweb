package com.example.demo.pojo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
//帖子
public class Post {
    private int postId; //帖子id
    private int userId; //发帖用户id
    private int bookID; //帖子对应的图书id
    private String title;   //标题
    private String content; //内容
    private String postImg;    // 封面URL
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
