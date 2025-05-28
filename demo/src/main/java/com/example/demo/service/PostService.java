package com.example.demo.service;

import com.example.demo.pojo.Post;

import java.util.List;

public interface PostService {
    Post createPost(Post post);

    List<Post> getPostsByBookId(Integer bookId);

    boolean deletePost(Integer postId);

    boolean updatePost(Post post);

    Post getPostById(Integer postId);
}
