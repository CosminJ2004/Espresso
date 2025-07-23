package com.example.demo.dto;

import com.example.demo.model.VoteType;

public class VoteDto {
    private Long userId;
    private Long postId;
    private Long commentId;
    private VoteType type;

    // Getters și Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getCommentId() { return commentId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }

    public VoteType getType() { return type; }
    public void setType(VoteType type) { this.type = type; }
}
