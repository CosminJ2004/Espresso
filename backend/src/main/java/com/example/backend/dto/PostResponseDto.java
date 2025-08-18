package com.example.backend.dto;

import com.example.backend.model.Post;
import com.example.backend.model.VoteType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private Long filter;
    private String author;
    private String subreddit;
    private Long upvotes;
    private Long downvotes;
    private Long score;
    private Long commentCount;
    private VoteType userVote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponseDto fromPost(Post post) {
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
