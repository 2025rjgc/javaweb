package com.example.demo.mapper;


import com.example.demo.entity.Comments;
import com.example.demo.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    // 获取某个圈子的帖子
    List<Post> selectPostsByCircleId(@Param("circleId") Integer circleId);

    // 获取帖子详情
    Post selectPostById(@Param("circleId") Integer circleId, @Param("postId") Integer postId);

    // 获取帖子详情
    List<Comments> selectCommentsByPostId(Integer postId);// 获取某条帖子的详细信息（含评论）

    // 创建帖子
    void insertPost(Post post);

    // 创建评论
    void insertComment(Comments comment);

    // 删除某个帖子的所有评论
    @Delete("delete from circle_post_comments where post_id=#{postId}")
    void deleteCommentByPostId(Integer postId);

    // 删除某个圈子的所有帖子以及评论
    @Delete("delete from circle_posts where circle_id=#{id} and id=#{postId}")
    void deletePostsByCircleId(@Param("id") Integer id,@Param("postId") Integer postId);

    //删除评论
    @Delete("delete from circle_post_comments where post_id=#{postId} and id=#{commentId}")
    void deleteComment(@Param("postId") Integer postId,@Param("commentId") Integer commentId);
}