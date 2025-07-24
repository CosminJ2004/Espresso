package com.example.demo.service;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository usersRepository;


    public List<CommentResponseDto> getCommentTreeForPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        // Mapam toate comentariile la DTO
        Map<Long, CommentResponseDto> dtoMap = new HashMap<>();
        List<CommentResponseDto> rootComments = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDto dto = new CommentResponseDto();
            dto.setId(comment.getId());
            dto.setText(comment.getText());
            dto.setAuthorUsername(comment.getAuthor().getUsername());
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

    public Comment addComment(CommentDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User author = usersRepository.findByUsername(dto.getAuthorUsername())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment comment = new Comment(author, dto.getText(), post, parent);
        return commentRepository.save(comment);
    }
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("Comment not found");
        }
        commentRepository.deleteById(commentId);
    }


}
