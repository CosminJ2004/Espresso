package com.example.backend.mapper;

import com.example.backend.dto.VoteResponseDto;
import com.example.backend.model.Comment;
import com.example.backend.model.Post;

public final class VoteMapper {

    private VoteMapper() {
    }

    public static VoteResponseDto toDto(Post post, String username) {
        if (post == null) {
            return null;
        }

        return new VoteResponseDto(
                post.getUpvoteCount(),
                post.getDownvoteCount(),
                post.getScore(),
                post.getUserVote(username)
        );
    }

    public static VoteResponseDto toDto(Comment comment, String username) {
        if (comment == null) {
            return null;
        }

        return new VoteResponseDto(
                comment.getUpvoteCount(),
                comment.getDownvoteCount(),
                comment.getScore(),
                comment.getUserVote(username)
        );
    }
}