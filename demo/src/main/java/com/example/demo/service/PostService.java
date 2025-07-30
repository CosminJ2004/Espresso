package com.example.demo.service;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.VoteRequestDto;
import com.example.demo.dto.VoteResponseDto;
import com.example.demo.model.*;
import com.example.demo.dto.PostDto;
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
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getUsername(), "echipa3_general", post.getUpvoteCount() , post.getDownvoteCount(), post.getScore(), post.getCommentCount(), null, post.getCreatedAt(), post.getUpdatedAt());
    }

    public PostDto addPost(PostDto dto) {
        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getAuthor()));

        Post post = new Post(author, dto.getTitle(), dto.getContent());

         postRepository.save(post);
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

    public boolean deletePost(Long id) {
        if (postRepository.existsById((long) id)) {
            postRepository.deleteById((long) id);
            return true;
        }
        return false;
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

    public CommentDto addComment(CommentDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User author = userRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment comment = new Comment(author, dto.getContent(), post, parent);
        commentRepository.save(comment);
        return dto;
    }

    public List<CommentDto> getCommentTreeForPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        // Mapam toate comentariile la DTO
        Map<Long, CommentDto> dtoMap = new HashMap<>();
        List<CommentDto> rootComments = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDto dto = new CommentDto();
            dto.setId(comment.getId());
            dto.setContent(comment.getText());
            dto.setAuthor(comment.getAuthor().getUsername());
            dto.setCreatedAt(comment.getCreatedAt());

            dtoMap.put(comment.getId(), dto);

            if (comment.getParent() != null) {
                CommentDto parentDto = dtoMap.get(comment.getParent().getId());
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
