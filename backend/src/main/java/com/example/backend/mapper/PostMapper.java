package com.example.backend.mapper;

import com.example.backend.dto.PostResponseDto;
import com.example.backend.model.Post;

public final class PostMapper {

    private PostMapper() {
    }

    public static PostResponseDto toDto(Post post) {
        if (post == null) {
            return null;
        }
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getFilter() != null ? post.getFilter().getId() : null,
                post.getAuthor().getUsername(),
                "echipa3_general",
                post.getUpvoteCount(),
                post.getDownvoteCount(),
                post.getScore(),
                post.getCommentCount(),
                post.getUserVote("current_user"),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}