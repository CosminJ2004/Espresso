package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.FilterRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import com.example.backend.util.logger.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final VoteRepository voteRepository;
    private final VoteService voteService;
    private final ProcessService processService;
    private final FilterRepository filterRepository;

    private static final LoggerManager loggerManager = LoggerManager.getInstance();

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, CommentService commentService, VoteRepository voteRepository, VoteService voteService, ProcessService processService, FilterRepository filterRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.voteRepository = voteRepository;
        this.voteService = voteService;
        this.processService = processService;
        this.filterRepository = filterRepository;
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::postToPostResponseDto)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
        return postToPostResponseDto(post);
    }

    @Transactional
    public PostResponseDto createPostWithoutImage(PostRequestDto postRequest) {
        User author = userRepository.findByUsername(postRequest.getAuthor())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Post post = new Post(author, postRequest.getTitle(), postRequest.getContent());
        postRepository.save(post);

        Vote authorVote = new Vote(author, post, VoteType.UP);
        voteRepository.save(authorVote);
        post.getVotes().add(authorVote);

        return postToPostResponseDto(post);
    }

    @Transactional
    public PostResponseDto createPostWithImage(PostRequestDto postRequest) {
        User author = userRepository.findByUsername(postRequest.getAuthor())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        Filter filter = filterRepository.findById(postRequest.getFilter())
                .orElseThrow(() -> new RuntimeException("Filter not found"));

        if (postRequest.getImage() == null || postRequest.getImage().isEmpty()) {
            throw new IllegalArgumentException("Image is required for createPostWithImage");
        }

        try {
            byte[] imageBytes = postRequest.getImage().getBytes();
            
            Post post = new Post(author, postRequest.getTitle(), postRequest.getContent(), filter);
            post = postRepository.save(post);

            processService.processImage(imageBytes, post.getFilter().getName(), post.getImageId());

            Vote authorVote = new Vote(author, post, VoteType.UP);
            voteRepository.save(authorVote);
            post.getVotes().add(authorVote);

            return postToPostResponseDto(post);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create post with image: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PostResponseDto updatePostWithoutImage (String id, PostRequestDto postRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        existingPost.setTitle(postRequest.getTitle());
        existingPost.setContent(postRequest.getContent());

        User author = userRepository.findByUsername(postRequest.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + postRequest.getAuthor()));
        existingPost.setAuthor(author);

        return postToPostResponseDto(postRepository.save(existingPost));
    }

    @Transactional
    public PostResponseDto updatePostWithImage(String id, PostRequestDto postRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        if (postRequest.getImage() == null || postRequest.getImage().isEmpty()) {
            throw new IllegalArgumentException("Image is required to update post with image");
        }

        try {
            existingPost.setTitle(postRequest.getTitle());
            existingPost.setContent(postRequest.getContent());

            User author = userRepository.findByUsername(postRequest.getAuthor())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + postRequest.getAuthor()));
            existingPost.setAuthor(author);

            if (postRequest.getFilter() != null) {
                Filter filter = filterRepository.findById(postRequest.getFilter())
                        .orElseThrow(() -> new RuntimeException("Filter not found"));
                existingPost.setFilter(filter);
            }

            Post updatedPost = postRepository.save(existingPost);

            byte[] imageBytes = postRequest.getImage().getBytes();
            String filterName = updatedPost.getFilter() != null ? updatedPost.getFilter().getName() : "none";
            processService.processImage(imageBytes, filterName, updatedPost.getImageId());

            return postToPostResponseDto(updatedPost);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update post with image: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletePost (String id){
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public VoteResponseDto votePost (String postId, VoteRequestDto voteRequest){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findByUsername("current_user")
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        voteService.vote(user, post, null, voteRequest.getVoteType());

        post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        VoteResponseDto voteResponse = new VoteResponseDto();
        voteResponse.setUpvotes(post.getUpvoteCount());
        voteResponse.setDownvotes(post.getDownvoteCount());
        voteResponse.setScore(post.getScore());

        Optional<Vote> currentVote = voteRepository.findByUserAndPost(user, post);
        voteResponse.setUserVote(currentVote.map(Vote::getType).orElse(VoteType.NONE));

        return voteResponse;
    }

    public List<CommentResponseDto> getCommentsByPostId (String postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }

        List<Comment> rootComments = commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);

        return rootComments.stream()
                .map(commentService::commentToCommentResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto addComment (String id, CommentRequestDto commentRequest){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User author = userRepository.findByUsername(commentRequest.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Comment parent = null;
        if (commentRequest.getParentId() != null) {
            parent = commentRepository.findById(commentRequest.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment comment = new Comment(author, commentRequest.getContent(), post, parent);
        comment = commentRepository.save(comment);

        Vote authorVote = new Vote(author, comment, VoteType.UP);
        voteRepository.save(authorVote);
        comment.getVotes().add(authorVote);

        return commentService.commentToCommentResponseDto(comment);
    }

    private PostResponseDto postToPostResponseDto (Post post){
        return new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getImageUrl(), post.getFilter() != null ? post.getFilter().getId() : null, post.getAuthor().getUsername(), "echipa3_general", post.getUpvoteCount(), post.getDownvoteCount(), post.getScore(), post.getCommentCount(), post.getUserVote(userRepository.findByUsername("current_user").orElseThrow()), post.getCreatedAt(), post.getUpdatedAt());
    }

    public String getLatency() {
        postRepository.getLatency();
        return "lol";
    }
}