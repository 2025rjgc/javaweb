package com.example.demo.controller;


import com.example.demo.entity.Comments;
import com.example.demo.entity.Post;
import com.example.demo.entity.Result;
import com.example.demo.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/circles")
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;

    // 获取某个圈子下的所有帖子
    @GetMapping("/{circleId}/posts")
    public Result getPostsByCircleId(@PathVariable Integer circleId) {
        List<Post> posts = postService.getPostsByCircleId(circleId);
        return Result.success(posts);
    }

    // 获取某条帖子的详细信息（含评论）
    @GetMapping("/{circleId}/post/{postId}")
    public Result getPostById(@PathVariable Integer circleId, @PathVariable Integer postId) {
        log.info("circleId: {}, postId: {}", circleId, postId);
        Post post = postService.getPostById(circleId, postId);

        //  判断帖子是否存在
        if (post == null) {
            return Result.success();
        }
        return Result.success(post);
    }

    //发帖子
    @PostMapping("/{circleId}/posts")
    public Result createPost(@PathVariable Integer circleId, @RequestBody Post post) {
        Post createdPost = postService.createPost(circleId, post);
        //后端打印信息
        log.info("createdPost: {}", createdPost);
        return Result.success();
    }
    //发评论
    @PostMapping("/{circleId}/posts/{postId}/comments")
    public Result createComment(@PathVariable Integer circleId, @PathVariable Integer postId, @RequestBody Comments comment) {
        Comments createdComment = postService.createComment(circleId, postId, comment);
        //后端打印信息
        log.info("createdComment: {}", createdComment);
        return Result.success();
    }
}
