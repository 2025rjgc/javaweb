package com.example.demo.service;



import com.example.demo.entity.Comments;
import com.example.demo.entity.Post;

import java.util.List;

public interface PostService {
    // 获取某个圈子下的所有帖子
    List<Post> getPostsByCircleId(Integer circleId);

    // 获取某条帖子的详细信息（含评论）
    Post getPostById(Integer circleId, Integer postId);

    // 创建帖子
    Post createPost(Integer circleId, Post post);

    // 创建评论
    Comments createComment(Integer circleId, Integer postId, Comments comment);

    // 删除帖子
    void deletePost(Integer circleId, Integer postId);

    // 删除评论
    void deleteComment(Integer postId, Integer commentId);
}
