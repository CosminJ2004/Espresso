package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import com.example.backend.util.logger.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    private final Path uploadDir = Paths.get("uploads/images");

    private static final LoggerManager loggerManager = LoggerManager.getInstance();

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, CommentService commentService, VoteRepository voteRepository, VoteService voteService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.voteRepository = voteRepository;
        this.voteService = voteService;
    }

    public List<PostResponseDto> getAllPosts() {

        loggerManager.log("file",LogLevel.INFO,"getting all posts");
        loggerManager.log("console", LogLevel.INFO, "getting all posts");
        loggerManager.log("console", LogLevel.ERROR, "getting all comments");

        return postRepository.findAll()
                .stream()
                .map(this::postToPostResponseDto)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
        return postToPostResponseDto(post);
    }

    public PostResponseDto createPost(PostRequestDto dto) {
        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthor()));

        Post post = new Post(author, dto.getTitle(), dto.getContent());
        postRepository.save(post);

        Vote authorVote = new Vote(author, post, VoteType.up);
        voteRepository.save(authorVote);
        loggerManager.log("console", LogLevel.INFO, "post created");
        return postToPostResponseDto(post);

    }
    public PostResponseDto createPostwithImage(PostRequestDto dto) throws IOException {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        post.setAuthor(author);

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String filename = UUID.randomUUID() + "_" + dto.getImage().getOriginalFilename();
            Path uploadPath = Paths.get("uploads/" + filename);
            Files.createDirectories(uploadPath.getParent());
            Files.copy(dto.getImage().getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            post.setFilePath("/uploads/" + filename); // linkul public
        }


        postRepository.save(post);
        return postToPostResponseDto(post);
    }

    public PostResponseDto updatePost(Long id, PostRequestDto dto) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        existingPost.setTitle(dto.getTitle());
        existingPost.setContent(dto.getContent());

        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthor()));
        existingPost.setAuthor(author);

        return postToPostResponseDto(postRepository.save(existingPost));
    }

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        postRepository.deleteById(id);
    }

    public VoteResponseDto votePost(Long postId, VoteRequestDto voteRequest) {
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
        voteResponse.setUserVote(currentVote.map(Vote::getType).orElse(VoteType.none));

        return voteResponse;
    }

    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }

        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        Map<Long, CommentResponseDto> dtoMap = new HashMap<>();
        List<CommentResponseDto> rootComments = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDto dto = commentService.commentToCommentResponseDto(comment);
            dtoMap.put(comment.getId(), dto);

            if (comment.getParent() != null) {
                CommentResponseDto parentDto = dtoMap.get(comment.getParent().getId());
                if (parentDto != null) {
                    parentDto.getReplies().add(dto);
                }
            } else {
                rootComments.add(dto);
            }
        }

        return rootComments;
    }

    public CommentResponseDto addComment(Long id, CommentRequestDto commentRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User author = userRepository.findByUsername(commentRequest.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Comment parent = null;
        if (commentRequest.getParentId() != null) {
            parent = commentRepository.findById(commentRequest.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment newComment = new Comment(author, commentRequest.getContent(), post, parent);
        Comment updatedComment = commentRepository.save(newComment);

        Vote authorVote = new Vote(author, updatedComment, VoteType.up);
        voteRepository.save(authorVote);

        return commentService.commentToCommentResponseDto(updatedComment);
    }

    private PostResponseDto postToPostResponseDto(Post post) {
        return new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getUsername(), "echipa3_general", post.getUpvoteCount() , post.getDownvoteCount(), post.getScore(), post.getCommentCount(), post.getUserVote(userRepository.findByUsername("current_user").orElseThrow()), post.getCreatedAt(), post.getUpdatedAt());
    }
}