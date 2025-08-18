package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.FilterRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import com.example.backend.util.logger.Logger;
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
    private final Logger log;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, CommentService commentService, VoteRepository voteRepository, VoteService voteService, ProcessService processService, FilterRepository filterRepository, Logger log) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.voteRepository = voteRepository;
        this.voteService = voteService;
        this.processService = processService;
        this.filterRepository = filterRepository;
        this.log = log;
    }

    public List<PostResponseDto> getAllPosts() {
        log.info("Fetching all posts");
        List<Post> allByOrderByCreatedAtDesc = postRepository.findAllByOrderByCreatedAtDesc();
        log.info("Retrieved " + allByOrderByCreatedAtDesc.size() + " posts");
        return allByOrderByCreatedAtDesc.stream().map(this::postToPostResponseDto).collect(Collectors.toList());
    }

    public PostResponseDto getPostById(String id) {
        log.info("Fetching post with ID: " + id);
        Post post = postRepository.findById(id).orElseThrow(() -> {
            log.error("Post not found with ID: " + id);
            return new IllegalArgumentException("Post not found with ID: " + id);
        });
        return postToPostResponseDto(post);
    }

    @Transactional
    public PostResponseDto createPostWithoutImage(PostRequestDto postRequest) {
        log.info("Creating post without image for author: " + postRequest.getAuthor());
        User author = userRepository.findByUsername(postRequest.getAuthor()).orElseThrow(() -> {
            log.error("Author not found: " + postRequest.getAuthor());
            return new RuntimeException("Author not found");
        });

        Post post = new Post(author, postRequest.getTitle(), postRequest.getContent());
        postRepository.save(post);
        log.info("Post created with ID: " + post.getId());

        Vote authorVote = new Vote(author, post, VoteType.UP);
        voteRepository.save(authorVote);
        post.getVotes().add(authorVote);

        return postToPostResponseDto(post);
    }

    @Transactional
    public PostResponseDto createPostWithImage(PostRequestDto postRequest) {
        log.info("Creating post with image for author: " + postRequest.getAuthor());
        User author = userRepository.findByUsername(postRequest.getAuthor()).orElseThrow(() -> new RuntimeException("Author not found"));
        Filter filter = filterRepository.findById(postRequest.getFilter()).orElseThrow(() -> new RuntimeException("Filter not found"));

        if (postRequest.getImage() == null || postRequest.getImage().isEmpty()) {
            throw new IllegalArgumentException("Image is required for createPostWithImage");
        }

        try {
            byte[] imageBytes = postRequest.getImage().getBytes();

            Post post = new Post(author, postRequest.getTitle(), postRequest.getContent(), filter);
            post = postRepository.save(post);
            log.info("Post with image created with ID: " + post.getId());

            processService.processImage(imageBytes, post.getFilter().getName(), post.getImageId());

            Vote authorVote = new Vote(author, post, VoteType.UP);
            voteRepository.save(authorVote);
            post.getVotes().add(authorVote);

            return postToPostResponseDto(post);

        } catch (Exception e) {
            log.error("Failed to create post with image", e);
            throw new RuntimeException("Failed to create post with image: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PostResponseDto updatePostWithoutImage(String id, PostRequestDto postRequest) {
        log.info("Updating post without image, ID: " + id);
        Post existingPost = postRepository.findById(id).orElseThrow(() -> {
            log.error("Post not found for update, ID: " + id);
            return new IllegalArgumentException("Post not found with ID: " + id);
        });

        existingPost.setTitle(postRequest.getTitle());
        existingPost.setContent(postRequest.getContent());

        User author = userRepository.findByUsername(postRequest.getAuthor()).orElseThrow(() -> new IllegalArgumentException("User not found: " + postRequest.getAuthor()));
        existingPost.setAuthor(author);

        Post updatedPost = postRepository.save(existingPost);
        log.info("Post updated successfully, ID: " + id);
        return postToPostResponseDto(updatedPost);
    }

    @Transactional
    public PostResponseDto updatePostWithImage(String id, PostRequestDto postRequest) {
        log.info("Updating post with image, ID: " + id);
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        if (postRequest.getImage() == null || postRequest.getImage().isEmpty()) {
            throw new IllegalArgumentException("Image is required to update post with image");
        }

        try {
            existingPost.setTitle(postRequest.getTitle());
            existingPost.setContent(postRequest.getContent());

            User author = userRepository.findByUsername(postRequest.getAuthor()).orElseThrow(() -> new IllegalArgumentException("User not found: " + postRequest.getAuthor()));
            existingPost.setAuthor(author);

            if (postRequest.getFilter() != null) {
                Filter filter = filterRepository.findById(postRequest.getFilter()).orElseThrow(() -> new RuntimeException("Filter not found"));
                existingPost.setFilter(filter);
            }

            Post updatedPost = postRepository.save(existingPost);

            byte[] imageBytes = postRequest.getImage().getBytes();
            String filterName = updatedPost.getFilter() != null ? updatedPost.getFilter().getName() : "none";
            processService.processImage(imageBytes, filterName, updatedPost.getImageId());

            log.info("Post with image updated successfully, ID: " + id);
            return postToPostResponseDto(updatedPost);

        } catch (Exception e) {
            log.error("Failed to update post with image, ID: " + id, e);
            throw new RuntimeException("Failed to update post with image: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletePost(String id) {
        log.info("Deleting post with ID: " + id);
        if (!postRepository.existsById(id)) {
            log.error("Post not found for deletion, ID: " + id);
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        postRepository.deleteById(id);
        log.info("Post deleted successfully, ID: " + id);
    }

    @Transactional
    public VoteResponseDto votePost(String postId, VoteRequestDto voteRequest) {
        log.info("Voting on post, ID: " + postId + ", vote type: " + voteRequest.getVoteType());
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findByUsername("current_user").orElseThrow(() -> new IllegalArgumentException("User not found"));

        voteService.vote(user, post, null, voteRequest.getVoteType());

        post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        VoteResponseDto voteResponse = new VoteResponseDto();
        voteResponse.setUpvotes(post.getUpvoteCount());
        voteResponse.setDownvotes(post.getDownvoteCount());
        voteResponse.setScore(post.getScore());

        Optional<Vote> currentVote = voteRepository.findByUserAndPost(user, post);
        voteResponse.setUserVote(currentVote.map(Vote::getType).orElse(VoteType.NONE));

        return voteResponse;
    }

    public List<CommentResponseDto> getCommentsByPostId(String postId) {
        log.info("Fetching comments for post ID: " + postId);
        if (!postRepository.existsById(postId)) {
            log.error("Post not found for comments, ID: " + postId);
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }

        List<Comment> rootComments = commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);
        log.info("Retrieved " + rootComments.size() + " comments for post ID: " + postId);

        return rootComments.stream().map(commentService::commentToCommentResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto addComment(String id, CommentRequestDto commentRequest) {
        log.info("Adding comment to post ID: " + id + " by author: " + commentRequest.getAuthor());
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User author = userRepository.findByUsername(commentRequest.getAuthor()).orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Comment parent = null;
        if (commentRequest.getParentId() != null) {
            parent = commentRepository.findById(commentRequest.getParentId()).orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment comment = new Comment(author, commentRequest.getContent(), post, parent);
        comment = commentRepository.save(comment);
        log.info("Comment created with ID: " + comment.getId());

        Vote authorVote = new Vote(author, comment, VoteType.UP);
        voteRepository.save(authorVote);
        comment.getVotes().add(authorVote);

        return commentService.commentToCommentResponseDto(comment);
    }

    private PostResponseDto postToPostResponseDto(Post post) {
        return PostResponseDto.fromPost(post);
    }
}