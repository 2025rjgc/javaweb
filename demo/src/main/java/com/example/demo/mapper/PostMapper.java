package com.example.demo.mapper;

import com.example.demo.pojo.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    void insertPost(Post post);

    List<Post> selectPostsByBookId(Integer bookId);

    int deletePostById(Integer postId);

    int updatePost(Post post);

    Post selectPostById(Integer postId);

    // 分页查询
    List<Post> selectPostsByPage(Integer start, Integer pageSize);

    // 多条件搜索
    List<Post> searchPosts(Post post);
}
