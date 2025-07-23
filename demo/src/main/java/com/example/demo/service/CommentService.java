package com.example.demo.service;

import com.example.demo.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {

    private final Map<Integer, Comment> allComments = new HashMap<>();
    private final PostService postService;
    private final VoteService voteService;
    private final UserService userService;

    @Autowired
    public CommentService(
            @Lazy PostService postService,
           @Lazy VoteService voteService,
           @Lazy UserService userService) {
        this.postService = postService;
        this.voteService = voteService;
        this.userService = userService;
    }





}
