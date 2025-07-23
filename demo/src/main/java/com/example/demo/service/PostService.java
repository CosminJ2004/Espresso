package com.example.demo.service;

import com.example.demo.dto.PostDto;
import com.example.demo.model.Post;

import com.example.demo.model.Users;

import com.example.demo.model.Comment;

import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    private PostDto convertToDto(Post post) {
        return new PostDto(post.getAuthor().getUsername(), post.getSummary(),post.getContent());
    }


    public Post addPost(PostDto dto) {
        Users author = userRepository.findByUsername(dto.getAuthorUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthorUsername()));

        Post post = new Post(author, dto.getSummary(), dto.getContent());
        return postRepository.save(post);
    }


    public PostDto getPostById(int id) {
        Post post = postRepository.findById((long) id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
        return convertToDto(post);
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public boolean deletePost(int id) {
        if (postRepository.existsById((long) id)) {
            postRepository.deleteById((long) id);
            return true;
        }
        return false;
    }


    public Post updatePost(int id, PostDto dto) {
        Post existingPost = postRepository.findById((long) id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        existingPost.setSummary(dto.getSummary());
        existingPost.setContent(dto.getContent());

        Users author = userRepository.findByUsername(dto.getAuthorUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthorUsername()));
        existingPost.setAuthor(author);

        return postRepository.save(existingPost);
    }




    public void addComment(Post post, Comment comment) {

//        commentsMap.computeIfAbsent(post.getId(), k -> new ArrayList<>()).add(comment);
    }

    public List<Comment> getComments(Post post) {

        return commentsMap.getOrDefault(post.getId(), new ArrayList<>());
    }


}
