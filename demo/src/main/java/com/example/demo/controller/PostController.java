package com.example.demo.controller;

import com.example.demo.pojo.Post;
import com.example.demo.service.PostService;
import com.example.demo.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService  postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 发布书评帖子
     */
    @PostMapping("/post")
    public Result publishPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        if (createdPost == null) {
            return Result.error("创建失败");
        }
        return Result.success(createdPost);
    }

    /**
     * 获取某本书的所有书评
     */
    @GetMapping("/book/{bookId}/posts")
    public Result getBookPosts(@PathVariable("bookId") Integer bookId) {
        List<Post> posts = postService.getPostsByBookId(bookId);
        return Result.success(posts);
    }

    /**
     * 删除书评（用户或管理员）
     */
    @DeleteMapping("/post/{postId}")
    public Result deletePost(@PathVariable("postId") Integer postId) {
        boolean success = postService.deletePost(postId);
        if (!success) {
            return Result.error("删除失败，可能该书评不存在");
        }
        return Result.success("删除成功");
    }

    /**
     * 修改书评内容
     */
    @PutMapping("/post")
    public Result updatePost(@RequestBody Post post) {
        boolean success = postService.updatePost(post);
        if (!success) {
            return Result.error("修改失败，可能该书评不存在");
        }
        return Result.success();
    }

    /**
     * 查看单个书评详情
     */
    @GetMapping("/post/{postId}")
    public Result getPostDetail(@PathVariable("postId") Integer postId) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            return Result.error("未找到该书评");
        }
        return Result.success(post);
    }
}