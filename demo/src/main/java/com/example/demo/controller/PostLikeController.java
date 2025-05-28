package com.example.demo.controller;

import com.example.demo.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostLikeController {
    private final PostLikeService postLikeService;
    @Autowired
    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    // 帖子点赞 @PostMapping("/postLike")
    // 取消点赞 @deleteMapping("/postLike/{id}")
    // 查看某个帖子点赞数 @getMapping("/postLike/{id}")

}
