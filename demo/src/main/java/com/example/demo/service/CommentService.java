package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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

    public Comment addComment(Users user, String text, Post post, Comment parent) {
        Comment comment = new Comment(user, text, post, parent);
        allComments.put(comment.getId(), comment);

        if (parent == null) {
            postService.addComment(post, comment);
            // Logging
            System.out.println("Comment ID " + comment.getId() + " added to post ID " + post.getId());
        } else {
            addReply(parent, comment);
            System.out.println("Reply ID " + comment.getId() + " added to parent comment ID " + parent.getId());
        }

        return comment;
    }

    public Comment getById(int id) {
        return allComments.get(id);
    }

    public void addReply(Comment parent, Comment reply) {
        parent.getReplies().add(reply);
        System.out.println("Reply ID " + reply.getId() + " added to comment ID " + parent.getId());
    }

    // În context Spring, evităm println direct în serviciu, dar pot rămâne pentru debugging.
    public String displayComment(Comment comment, int level) {
        int votes = voteService.getVoteCount(comment);
        String indent = " ".repeat(level * 2);
        String result = indent + comment.getText() + " written by " + comment.getAuthor().getUsername()
                + " Comment ID: " + comment.getId() + " Votes: " + votes + " | Posted on: "
                + comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n";

        for (Comment reply : comment.getReplies()) {
            result += displayComment(reply, level + 1);
        }

        return result;
    }

    // Metoda care adaugă comment direct, primind parametrii (nu consolă)
    public Comment addCommentToPost(Users user, String text, Post post) {
        return addComment(user, text, post, null);
    }

    // Adaugă reply la un comment
    public Comment addCommentToComment(Users user, String text, int parentCommentId) {
        Comment parent = allComments.get(parentCommentId);
        if (parent == null) {
            throw new IllegalArgumentException("Parent comment not found");
        }
        return addComment(user, text, parent.getPost(), parent);
    }

}
