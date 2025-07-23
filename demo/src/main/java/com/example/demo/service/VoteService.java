package com.example.demo.service;
import com.example.demo.model.*;
import com.example.demo.util.Console;
import com.example.demo.util.logger.Log;
import com.example.demo.util.logger.LogLevel;
import org.springframework.stereotype.Service;
//import util.Console;

import java.util.HashMap;
import java.util.Map;

@Service
public class VoteService {


    private final Map<Votable, Integer> voteCounts = new HashMap<>();

    // No static instance; Spring manages the bean lifecycle

    public int getVoteCount(Votable votable) {
        return voteCounts.getOrDefault(votable, 0);
    }




    private final UserService userService;
    private final CommentService commentService;

    public VoteService(UserService userService, CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }




}
