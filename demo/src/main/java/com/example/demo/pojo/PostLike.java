package com.example.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 帖子点赞
public class PostLike {
    private int likeId;
    private int userId;
    private int postId;
    private LocalDateTime createTime;
}
