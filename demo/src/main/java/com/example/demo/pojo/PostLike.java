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
    private Integer likeId;
    private Integer userId;
    private Integer postId;
    private LocalDateTime createTime;
}
