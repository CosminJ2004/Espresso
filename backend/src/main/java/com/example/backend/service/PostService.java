package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import com.example.backend.util.logger.ConsoleLogger;
import com.example.backend.util.logger.LogLevel;
import com.example.backend.util.logger.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final VoteService voteService;
    private final LoggerManager loggerManager = LoggerManager.getInstance();
    private final ConsoleLogger consoleLogger = new ConsoleLogger(LogLevel.DEBUG);

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository,  CommentRepository commentRepository, VoteRepository voteRepository, VoteService voteService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
        this.voteService = voteService;
    }

    public List<PostResponseDto> getAllPosts() {
        loggerManager.addLogger(consoleLogger);
        loggerManager.log(LogLevel.INFO,"GET for all posts");
        return postRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
        return convertToResponseDto(post);
    }

    public PostResponseDto createPost(PostRequestDto dto) {
        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthor()));

        Post post = new Post(author, dto.getTitle(), dto.getContent());
        postRepository.save(post);
        
        Vote authorVote = new Vote(author, post, VoteType.up);
        voteRepository.save(authorVote);
        loggerManager.addLogger(consoleLogger);
        loggerManager.log(LogLevel.INFO, "post created");
        return convertToResponseDto(post);
    }

    public PostResponseDto updatePost(Long id, PostRequestDto dto) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        existingPost.setTitle(dto.getTitle());
        existingPost.setContent(dto.getContent());

        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthor()));
        existingPost.setAuthor(author);

        return convertToResponseDto(postRepository.save(existingPost));
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
            CommentResponseDto dto = commentToCommentResponseDto(comment);
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

        return commentToCommentResponseDto(updatedComment);
    }

    private CommentResponseDto commentToCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponse = new CommentResponseDto();
        commentResponse.setId(comment.getId());
        commentResponse.setPostId(comment.getPost().getId());
        commentResponse.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        commentResponse.setContent(comment.getText());
        commentResponse.setAuthor(comment.getAuthor().getUsername());
        commentResponse.setUpvotes(comment.getUpvoteCount());
        commentResponse.setDownvotes(comment.getDownvoteCount());
        commentResponse.setScore(comment.getScore());

        // hardcoded - current_user
        User currentUser = userRepository.findByUsername("current_user").orElse(null);
        commentResponse.setUserVote(currentUser != null ? comment.getUserVote(currentUser) : null);

        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setUpdatedAt(comment.getUpdatedAt());
        commentResponse.setReplies(new ArrayList<>());

        return commentResponse;
    }

    private PostResponseDto convertToResponseDto(Post post) {
        return new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getUsername(), "echipa3_general", post.getUpvoteCount() , post.getDownvoteCount(), post.getScore(), post.getCommentCount(), post.getUserVote(userRepository.findByUsername("current_user").orElseThrow()), post.getCreatedAt(), post.getUpdatedAt());
    }
}

//public Post addPostWithImage(PostRequestDto dto, String imagePath) {
//    User author = userRepository.findByUsername(dto.getAuthor())
//            .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthor()));
//
//    Post post = new Post(author, dto.getTitle(), dto.getContent(),imagePath);
//    post.setFilePath(imagePath);
//    return postRepository.save(post);
//}
//
//public String saveImage(MultipartFile file) throws IOException {
//    if (file.isEmpty()) throw new IOException("Empty file.");
//
//    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//    Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
//    Files.createDirectories(uploadDir);
//    Path targetPath = uploadDir.resolve(fileName);
//    Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//
//    return "/uploads/" + fileName; // sau doar fileName dacă preferi
//}