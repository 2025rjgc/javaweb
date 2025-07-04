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
        log.debug("circleId: {}, postId: {}", circleId, postId);
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
        log.debug("createdPost: {}", createdPost);
        return Result.success();
    }
    //发评论
    @PostMapping("/{circleId}/posts/{postId}/comments/{userId}")
    public Result createComment(@PathVariable Integer circleId,
                                @PathVariable Integer postId,
                                @PathVariable Integer userId,
                                @RequestBody Comments comment) {
        Comments createdComment = postService.createComment(circleId, postId, userId, comment);
        //后端打印信息
        log.info("createdComment: {}", createdComment);
        return Result.success();
    }
    //删除帖子以及评论
    @DeleteMapping("/{circleId}/posts/{postId}")
    public Result deletePost(@PathVariable Integer circleId, @PathVariable Integer postId) {
        log.info("删除帖子,circleId: {}, postId: {}", circleId, postId);
        postService.deletePost(circleId, postId);
        return Result.success();
    }
    // 删除评论
    @DeleteMapping("/{circleId}/posts/{postId}/comments/{commentId}")
    public Result deleteComment(@PathVariable Integer circleId, @PathVariable Integer postId, @PathVariable Integer commentId) {
        log.info("删除评论,circleId: {}, postId: {}, commentId: {}", circleId, postId, commentId);
        postService.deleteComment(postId, commentId);
        return Result.success();
    }
}
