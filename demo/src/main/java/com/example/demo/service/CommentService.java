package com.example.demo.service;

import com.example.demo.dto.CommentDto;
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

    public Comment addComment(CommentDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User author = usersRepository.findByUsername(dto.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment comment = new Comment(author, dto.getContent(), post, parent);
        return commentRepository.save(comment);
    }
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("Comment not found");
        }
        commentRepository.deleteById(commentId);
    }


}
