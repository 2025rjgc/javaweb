package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 评论
public class Comment {
    private Integer commentId;
    private Integer userId;
    private Integer postId;
    private String comment;
    private LocalDateTime createTime;
}
