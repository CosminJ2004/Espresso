package com.example.backend.mapper;

import com.example.backend.dto.CommentResponseDto;
import com.example.backend.model.Comment;

import java.util.ArrayList;
import java.util.List;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentResponseDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        // Convert replies recursively
        List<CommentResponseDto> replies = new ArrayList<>();
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            replies = comment.getReplies().stream()
                    .map(CommentMapper::toDto)
                    .toList();
        }

        return new CommentResponseDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getText(),
                comment.getAuthor().getUsername(),
                comment.getUpvoteCount(),
                comment.getDownvoteCount(),
                comment.getScore(),
                comment.getUserVote("current_user"),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                replies
        );
    }
}