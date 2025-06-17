package com.example.demo.service.impl;


import com.example.demo.entity.Comments;
import com.example.demo.entity.Post;
import com.example.demo.mapper.CircleMapper;
import com.example.demo.mapper.PostMapper;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private CircleMapper circleMapper;
    @Autowired
    private UserService userService;

    // 获取某个圈子下的所有帖子
    @Override
    public List<Post> getPostsByCircleId(Integer circleId) {
        List<Post> posts = postMapper.selectPostsByCircleId(circleId);
        for (Post post : posts){
            log.debug("获取圈子下的所有帖子{}",post);
            post.setAvatar(userService.findByName(post.getUsername()).getAvatar());
        }
        log.debug("获取圈子下的所有帖子{}",posts);
        return posts;
    }

    // 获取某条帖子的详细信息（含评论）
    @Override
    public Post getPostById(Integer circleId, Integer postId) {
        Post post = postMapper.selectPostById(circleId, postId);
        List<Comments> comment = postMapper.selectCommentsByPostId(postId);
        // 判断评论列表是否为空
        if (comment == null) {
            return post;
        }
        post.setAvatar(userService.findByName(post.getUsername()).getAvatar());
        post.setComments(comment);
        return post;
    }

    // 创建帖子
    @Override
    public Post createPost(Integer circleId, Post post) {
        // 判断username 和 content
        if (post.getUsername() == null || post.getContent() == null) {
            throw new IllegalArgumentException("Missing required fields: username, content");
        }

        // 直接使用当前时间，无需格式化再解析
        LocalDateTime currentTime = LocalDateTime.now();

        post.setCircleId(circleId); // 设置所属圈子ID
        post.setPostTime(currentTime);// 设置发布时间
        postMapper.insertPost(post);

        //创建帖子后，更新圈子的帖子数
        int postCount = circleMapper.countPostsByCircleId(circleId);
        circleMapper.updatePosts(circleId, postCount);
        log.debug("刷新圈子 {} 的帖子计数为 {}", circleId, postCount);
        return post;
    }

    // 创建评论
    @Override
    public Comments createComment(Integer circleId, Integer postId, Integer userId,Comments comment) {
        Post post = postMapper.selectPostById(circleId, postId);
        //  判断帖子是否存在
        if (post == null) {
            throw new IllegalArgumentException("无当前帖子");
        }

        //  判断评论内容
        if (comment.getUsername() == null || comment.getContent() == null) {
            throw new IllegalArgumentException("无");
        }

        // 直接获取当前时间，无需格式化再解析
        LocalDateTime currentTime = LocalDateTime.now();

        comment.setUsername(userService.findById(userId).getUsername());
        comment.setPostId(postId);// 设置所属帖子ID
        comment.setCommentTime(currentTime);//  设置发布时间

        postMapper.insertComment(comment);

        return comment;
    }
    // 删除帖子
    @Override
    public void deletePost(Integer circleId, Integer postId) {
        log.debug("删除帖子,circleId: {}, postId: {}", circleId, postId);
        postMapper.deleteCommentByPostId(postId);
        postMapper.deletePostsByCircleId(circleId, postId);

        // 更新圈子的帖子数
        int postCount = circleMapper.countPostsByCircleId(circleId);
        circleMapper.updatePosts(circleId, postCount);
        log.debug("圈子{}的帖子计数为 {}", circleId, postCount);
    }

    // 删除评论
    @Override
    public void deleteComment(Integer postId, Integer commentId) {
        postMapper.deleteComment(postId, commentId);
    }
}