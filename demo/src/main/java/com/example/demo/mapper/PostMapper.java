package com.example.demo.mapper;

import com.example.demo.pojo.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    // 插入帖子
    void insertPost(Post post);

    // 查询帖子
    List<Post> selectPostsByBookId(Integer bookId);

    // 删除帖子
    int deletePostById(Integer postId);

    // 更新帖子
    int updatePost(Post post);

    // 查询帖子
    Post selectPostById(Integer postId);

    // 分页查询
    List<Post> selectPostsByPage(Integer start, Integer pageSize);

    // 多条件搜索
    List<Post> searchPosts(Post post);
}
