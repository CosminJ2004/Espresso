package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VoteRepository;

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
    private final VoteRepository voteRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository,  CommentRepository commentRepository, VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
    }

    private PostDto convertToDto(Post post) {
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getUsername(), "echipa3_general", post.getUpvoteCount() , post.getDownvoteCount(), post.getScore(), post.getCommentCount(), post.getUserVote(userRepository.findByUsername("current_user").orElseThrow()), post.getCreatedAt(), post.getUpdatedAt());
    }

    public PostDto createPost(PostDto dto) {
        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthor()));

        Post post = new Post(author, dto.getTitle(), dto.getContent());
        postRepository.save(post);
        
        Vote authorVote = new Vote(author, post, VoteType.up);
        voteRepository.save(authorVote);
        return convertToDto(post);
    }

    public Post addPostWithImage(PostDto dto, String imagePath) {
        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthor()));

        Post post = new Post(author, dto.getTitle(), dto.getContent(),imagePath);
        post.setFilePath(imagePath);
        return postRepository.save(post);
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IOException("Empty file.");

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);
        Path targetPath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + fileName; // sau doar fileName dacă preferi
    }

    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
        return convertToDto(post);
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        postRepository.deleteById(id);
    }

    public Post updatePost(Long id, PostDto dto) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        existingPost.setTitle(dto.getTitle());
        existingPost.setContent(dto.getContent());

        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthor()));
        existingPost.setAuthor(author);

        return postRepository.save(existingPost);
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

        return toCommentReponseDto(updatedComment);
    }

    private CommentResponseDto toCommentReponseDto(Comment comment) {
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

    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        // Mapam toate comentariile la DTO
        Map<Long, CommentResponseDto> dtoMap = new HashMap<>();
        List<CommentResponseDto> rootComments = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDto dto = new CommentResponseDto();
            dto.setId(comment.getId());
            dto.setContent(comment.getText());
            dto.setAuthor(comment.getAuthor().getUsername());
            dto.setCreatedAt(comment.getCreatedAt());

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

    public VoteResponseDto votePost(Long postId, VoteRequestDto voteRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findByUsername("current_user")
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<Vote> existingVote = voteRepository.findByUserAndPost(user, post);

        if (voteRequest.getVoteType() == null || voteRequest.getVoteType() == VoteType.none) {
            existingVote.ifPresent(voteRepository::delete);
        } else {
            if (existingVote.isPresent()) {
                Vote vote = existingVote.get();
                vote.setType(voteRequest.getVoteType());
                voteRepository.save(vote);
            } else {
                Vote newVote = new Vote(user, post, voteRequest.getVoteType());
                voteRepository.save(newVote);
            }
        }

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
}
