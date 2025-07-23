package com.example.demo.service;

import com.example.demo.dto.PostDto;
import com.example.demo.model.Post;

import com.example.demo.model.Users;

import com.example.demo.model.Comment;

import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.example.demo.logger.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PostService {

    private static final int MIN_VOTES_FOR_STAR = 3;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final VoteService voteService;
    private final CommentService commentService;
    private final UserRepository userRepository;
    private static final Map<Integer, List<Comment>> commentsMap = new HashMap<>();

    public PostService(PostRepository postRepository, VoteService voteService, @Lazy CommentService commentService, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.voteService = voteService;
        this.commentService = commentService;
        this.userRepository = userRepository;
    }
    public Post addPost(PostDto dto) {
        Users author = userRepository.findByUsername(dto.getAuthorUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthorUsername()));

        Post post = new Post(author, dto.getSummary(), dto.getContent());
        return postRepository.save(post);
    }
//
//    public void addPost(Post post) {
//        boolean saved = postRepository.save(post);
//        if (saved) {
//            commentsMap.put(post.getId(), new ArrayList<>());
//            Log.log(LogLevel.INFO, "Post saved to database with ID: " + post.getId());
//        } else {
//            Log.log(LogLevel.ERROR, "Failed to save post to database");
//        }
//    }

    public Post getPostById(int id) {
        return postRepository.findById((long) id).orElse(null);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

//    public boolean deletePostById(int postId) {
//        if (postRepository.findById((long) postId).isEmpty()) {
//            return false;
//        }
//
//        boolean deleted = postRepository.deleteById(Long.valueOf(postId));
//        if (deleted) {
//            commentsMap.remove(postId);
//            Log.log(LogLevel.INFO, "Post deleted from database with ID: " + postId);
//        } else {
//            Log.log(LogLevel.ERROR, "Failed to delete post from database with ID: " + postId);
//        }
//        return deleted;
//    }


    public void addComment(Post post, Comment comment) {

        commentsMap.computeIfAbsent(post.getId(), k -> new ArrayList<>()).add(comment);
    }

    public List<Comment> getComments(Post post) {

        return commentsMap.getOrDefault(post.getId(), new ArrayList<>());
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

//    public void expand(Post post) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        String formattedDateTime = post.getCreatedAt().format(formatter);
//        int votes = voteService.getVoteCount(post);
//
//
//        String result = "[" + post.getId() + "] " + post.getSummary() + " " + post.getContent() + "; (by " + post.getAuthor().getUsername() + ") Votes: " + votes + " | Posted on: " + formattedDateTime;
//        if (votes >= MIN_VOTES_FOR_STAR) {
//            result += " ⭐";
//        }
//        System.out.println(result);
//
//        List<Comment> commentList = getComments(post);
//
//
//        if (commentList.isEmpty()) {
//            System.out.println("There are no comments. \n");
//        } else {
//            CommentService commentService = CommentService.getInstance();
//            for (Comment comment : commentList) {
//                commentService.displayComment(comment, 1);
//            }
//        }
//    }

    public void showAllPosts()
    {
        for (Post post : getAllPosts()) {
            System.out.println(display(post));
        }
    }

//    public void createPostUI() {
//        Log.log(LogLevel.INFO, "Creating new post for user: " + UserService.getCurrentUser().getUsername());
//        String summary = Console.readText("Enter summary:");
//        String content = Console.readText("Enter content:");
//
//        Post post = new Post(UserService.getCurrentUser(), summary, content);
//        addPost(post);
//
//        System.out.println("Post created with ID: " + post.getId());
//    }

//    public Post expandPostUI() {
//        int postId = Console.readInt("Enter post ID to expand: ");
//
//        Post post = getPostById(postId);
//        if (post == null) {
//            System.out.println("Post not found.");
//            return null;
//        }
//
//        expand(post);
//        return post;
//    }
//
//    public void deletePostUI(Post post) {
//
//        Log.log(LogLevel.INFO, "User attempting to delete post ID: " + post.getId());
//
//        if (post == null) {
//            System.out.println("Post not found.");
//            return;
//        }
//
//        Log.log(LogLevel.INFO, "User attempting to delete post ID: " + post.getId());
//
//        if (post.getAuthor().equals(UserService.getCurrentUser())) {
//            boolean deleted = deletePostById(post.getId());
//            if (deleted) {
//                Log.log(LogLevel.INFO, "Post deleted successfully - ID: " + post.getId() + " by user: " + UserService.getCurrentUser().getUsername());
//                System.out.println("Post deleted successfully.");
//            } else {
//                System.out.println("Failed to delete post.");
//            }
//        } else {
//            Log.log(LogLevel.WARN, "Unauthorized delete attempt on post ID: " + post.getId() + " by user: " + UserService.getCurrentUser().getUsername());
//            System.out.println("You can only delete your own posts.");
//        }
//    }
//
//    // Pentru testare: poți adăuga reset()
//    public static void resetInstance() {
//        instance = null;
//    }
}
