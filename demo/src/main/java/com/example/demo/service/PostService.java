package com.example.demo.service;

import com.example.demo.model.Post;

import com.example.demo.model.Vote;

import com.example.demo.model.Comment;

import com.example.demo.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private static final int MIN_VOTES_FOR_STAR = 3;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final VoteService voteService;
    private final CommentService commentService;

    public PostService(PostRepository postRepository, VoteService voteService, CommentService commentService) {
        this.postRepository = postRepository;
        this.voteService = voteService;
        this.commentService = commentService;
    }

    public boolean addPost(Post post) {
        try {
            postRepository.save(post);
            logger.info("Post saved to database with ID: {}", post.getId());
            return true;
        } catch (Exception e) {
            logger.error("Failed to save post to database", e);
            return false;
        }
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public boolean deletePostById(Long postId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            logger.warn("Attempted to delete non-existing post with ID: {}", postId);
            return false;
        }
        try {
            postRepository.deleteById(postId);
            logger.info("Post deleted from database with ID: {}", postId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to delete post from database with ID: {}", postId, e);
            return false;
        }
    }

    public void addComment(Post post, Comment comment) {
        // Assuming Comment entity manages the post relationship (mappedBy),
        // and CommentRepository will be used for persistence in a real app
        post.getComments().add(comment);
        // Optionally save comment using CommentRepository here if needed
        logger.info("Added comment ID {} to post ID {}", comment.getId(), post.getId());
    }

    public List<Comment> getComments(Post post) {
        // Assuming Post entity has a getComments() method mapped as a relationship
        return post.getComments();
    }

    public String display(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = post.getCreatedAt().format(formatter);
        int votes = voteService.getVoteCount(post);

        String result = "[" + post.getId() + "] " + post.getSummary() + " (by " + post.getAuthor().getUsername() + ") Votes: " + votes + " | Posted on: " + formattedDateTime;
        if (votes >= MIN_VOTES_FOR_STAR) {
            result += " ⭐";
        }
        return result;
    }

    public void expand(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = post.getCreatedAt().format(formatter);
        int votes = voteService.getVoteCount(post);

        String result = "[" + post.getId() + "] " + post.getSummary() + " " + post.getContent() + "; (by " + post.getAuthor().getUsername() + ") Votes: " + votes + " | Posted on: " + formattedDateTime;
        if (votes >= MIN_VOTES_FOR_STAR) {
            result += " ⭐";
        }
        System.out.println(result);

        List<Comment> commentList = getComments(post);

        if (commentList.isEmpty()) {
            System.out.println("There are no comments. \n");
        } else {
            for (Comment comment : commentList) {
                commentService.displayComment(comment, 1);
            }
        }
    }

}
