package com.example.demo.controller;

import com.example.demo.pojo.Post;
import com.example.demo.service.PostService;
import com.example.demo.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 书评帖子控制器，提供发布、查看、修改、删除等功能。
 */
@RestController
@RequestMapping("/post")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Value("${app.postTxt-upload-dir}")
    private String postTxtUploadDir;

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 发布新的书评帖子。
     *
     * @param post 包含书ID、用户ID、标题、内容等字段的书评对象
     * @return 创建成功返回帖子详情，失败返回错误信息
     */
    @PostMapping("/newPost")
    public Result publishPost(@RequestBody Post post) {
        logger.info("尝试发布新书评: {}", post);

        if (post == null || post.getUserId() <= 0 || post.getBookId() <= 0 ||
                post.getTitle() == null || post.getContent() == null) {
            logger.warn("参数缺失或无效");
            return Result.error("参数缺失或无效");
        }

        Post createdPost = postService.createPost(post);
        if (createdPost == null) {
            logger.warn("创建书评失败");
            return Result.error("创建失败");
        }
        logger.info("书评发布成功: postId={}", createdPost.getPostId());
        return Result.success(createdPost);
    }

    /**
     * 获取某本书的所有书评。
     *
     * @param bookId 书籍 ID
     * @return 返回该书对应的所有书评列表
     */
    @GetMapping("/getPostsOfBook/{bookId}")
    public Result getBookPosts(@PathVariable("bookId") Integer bookId) {
        logger.info("获取书籍 [{}] 的书评", bookId);

        if (bookId == null || bookId <= 0) {
            logger.warn("无效的 bookId");
            return Result.error("无效的书籍ID");
        }

        List<Post> posts = postService.getPostsByBookId(bookId);
        logger.info("找到 {} 条书评", posts.size());
        return Result.success(posts);
    }

    /**
     * 删除指定 ID 的书评（用户或管理员均可调用）。
     *
     * @param postId 书评 ID
     * @return 删除成功返回 success，失败返回错误信息
     */
    @DeleteMapping("/delete/{postId}")
    public Result deletePost(@PathVariable("postId") Integer postId) {
        logger.info("尝试删除书评: postId={}", postId);

        if (postId == null || postId <= 0) {
            logger.warn("无效的 postId");
            return Result.error("无效的书评ID");
        }

        boolean success = postService.deletePost(postId);
        if (!success) {
            logger.warn("删除书评失败，可能不存在: postId={}", postId);
            return Result.error("删除失败，可能该书评不存在");
        }
        logger.info("书评删除成功: postId={}", postId);
        return Result.success("删除成功");
    }

    /**
     * 修改已有书评内容。
     *
     * @param post 包含 postId、title、content 等字段的书评对象
     * @return 修改成功返回 success，失败返回错误信息
     */
    @PutMapping("/setPost")
    public Result updatePost(@RequestBody Post post) {
        logger.info("尝试修改书评: {}", post);

        if (post == null || post.getPostId() == null || post.getPostId() <= 0) {
            logger.warn("缺少必要参数：postId");
            return Result.error("缺少必要参数：postId");
        }

        boolean success = postService.updatePost(post);
        if (!success) {
            logger.warn("修改书评失败，可能不存在: postId={}", post.getPostId());
            return Result.error("修改失败，可能该书评不存在");
        }
        logger.info("书评修改成功: postId={}", post.getPostId());
        return Result.success();
    }

    /**
     * 查看单个书评详情。
     *
     * @param postId 书评 ID
     * @return 成功返回书评对象，失败返回错误信息
     */
    @GetMapping("/getPost/{postId}")
    public Result getPostDetail(@PathVariable("postId") Integer postId) {
        logger.info("获取书评详情: postId={}", postId);

        if (postId == null || postId <= 0) {
            logger.warn("无效的 postId");
            return Result.error("无效的书评ID");
        }

        Post post = postService.getPostById(postId);
        if (post == null) {
            logger.warn("未找到书评: postId={}", postId);
            return Result.error("未找到该书评");
        }
        return Result.success(post);
    }

    @GetMapping("/content")
    public ResponseEntity<byte[]> getPostContent(@RequestParam("file") String fileName) {
        try {
            java.nio.file.Path filePath = java.nio.file.Paths.get(postTxtUploadDir + fileName);
            byte[] content = java.nio.file.Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain;charset=UTF-8")
                    .body(content);
        } catch (Exception e) {
            logger.error("读取内容文件失败", e);
            return ResponseEntity.notFound().build();
        }
    }
}