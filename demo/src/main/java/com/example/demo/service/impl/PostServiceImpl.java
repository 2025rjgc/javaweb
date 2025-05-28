package com.example.demo.service.impl;

import com.example.demo.mapper.PostMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.Post;
import com.example.demo.pojo.User;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Autowired
    public PostServiceImpl(PostMapper postMapper, UserMapper userMapper) {
        this.postMapper = postMapper;
        this.userMapper = userMapper;
    }
    @Override
    public Post createPost(Post post) {
        User user = new User();
        user.setUserId(post.getUserId());
        User user1 = userMapper.find(user).get(0);
        if (user1 == null) {
            System.out.println("用户不存在");
            return null;
        }
        post.setUserId(user1.getUserId());
        postMapper.insertPost(post);
        return post;
    }
    @Override
    public List<Post> getPostsByBookId(Integer bookId) {
        return postMapper.selectPostsByBookId(bookId);
    }
    @Override
    public boolean deletePost(Integer postId) {
        return postMapper.deletePostById(postId) > 0;
    }
    @Override
    public boolean updatePost(Post post) {
        return postMapper.updatePost(post) > 0;
    }
    @Override
    public Post getPostById(Integer postId) {
        return postMapper.selectPostById(postId);
    }
}
