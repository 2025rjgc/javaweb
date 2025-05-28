package com.example.demo.service.impl;

import com.example.demo.mapper.PostLikeMapper;
import com.example.demo.mapper.PostMapper;
import com.example.demo.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeMapper postLikeMapper;
    private final PostMapper postMapper;
    @Autowired
    public PostLikeServiceImpl(PostLikeMapper postLikeMapper, PostMapper postMapper) {
        this.postLikeMapper = postLikeMapper;
        this.postMapper = postMapper;
    }

}
