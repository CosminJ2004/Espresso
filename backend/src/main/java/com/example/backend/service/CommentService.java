package com.example.backend.service;

import com.example.backend.dto.CommentRequestDto;
import com.example.backend.dto.CommentResponseDto;
import com.example.backend.dto.VoteRequestDto;
import com.example.backend.dto.VoteResponseDto;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.model.Vote;
import com.example.backend.model.VoteType;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteService voteService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, VoteRepository voteRepository, VoteService voteService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.voteService = voteService;
    }

    public CommentResponseDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + id));

        return convertToResponseDto(comment);
    }

    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + id));

        comment.setText(commentRequest.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return convertToResponseDto(updatedComment);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("Comment not found");
        }
        commentRepository.deleteById(commentId);
    }

    public VoteResponseDto voteComment(Long commentId, VoteRequestDto voteRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

//        hardcoded - current_user
        User user = userRepository.findByUsername("current_user")
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        voteService.vote(user, null, comment, voteRequest.getVoteType());

        comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        VoteResponseDto voteResponse = new VoteResponseDto();
        voteResponse.setUpvotes(comment.getUpvoteCount());
        voteResponse.setDownvotes(comment.getDownvoteCount());
        voteResponse.setScore(comment.getScore());

        Optional<Vote> currentVote = voteRepository.findByUserAndComment(user, comment);
        voteResponse.setUserVote(currentVote.map(Vote::getType).orElse(VoteType.none));

        return voteResponse;
    }

    private CommentResponseDto convertToResponseDto(Comment comment) {
        CommentResponseDto commentResponse = new CommentResponseDto();
        commentResponse.setId(comment.getId());
        commentResponse.setPostId(comment.getPost().getId());
        commentResponse.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        commentResponse.setContent(comment.getText());
        commentResponse.setAuthor(comment.getAuthor().getUsername());
        commentResponse.setUpvotes(comment.getUpvoteCount());
        commentResponse.setDownvotes(comment.getDownvoteCount());
        commentResponse.setScore(comment.getScore());

//        hardcoded - current_user
        User user = userRepository.findByUsername("current_user").orElse(null);
        commentResponse.setUserVote(user != null ? comment.getUserVote(user) : null);

        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setUpdatedAt(comment.getUpdatedAt());

        return commentResponse;
    }
}
