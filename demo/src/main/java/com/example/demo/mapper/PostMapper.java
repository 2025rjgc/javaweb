package com.example.demo.mapper;


import com.example.demo.entity.Comments;
import com.example.demo.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    List<Post> selectPostsByCircleId(@Param("circleId") Integer circleId);

    Post selectPostById(@Param("circleId") Integer circleId, @Param("postId") Integer postId);

    List<Comments> selectCommentsByPostId(Integer postId);// 获取某条帖子的详细信息（含评论）

    void insertPost(Post post);

    void insertComment(Comments comment);
}