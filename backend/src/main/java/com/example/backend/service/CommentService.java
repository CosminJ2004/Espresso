package com.example.backend.service;

import com.example.backend.dto.CommentRequestDto;
import com.example.backend.exception.*;
import com.example.backend.dto.CommentResponseDto;
import com.example.backend.dto.VoteRequestDto;
import com.example.backend.dto.VoteResponseDto;
import com.example.backend.mapper.VoteMapper;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.mapper.CommentMapper;
import com.example.backend.util.logger.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final VoteService voteService;
    private final Logger log;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, VoteService voteService, Logger log) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.voteService = voteService;
        this.log = log;
    }

    public CommentResponseDto getCommentById(String id) {
        log.info("Fetching comment with ID: " + id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found with ID: " + id);
                    return new CommentNotFoundException(id);
                });

        return CommentMapper.toDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(String id, CommentRequestDto commentRequest) {
        log.info("Updating comment with ID: " + id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found for update, ID: " + id);
                    return new CommentNotFoundException(id);
                });

        comment.setText(commentRequest.getContent());
        Comment updatedComment = commentRepository.save(comment);
        log.info("Comment updated successfully, ID: " + id);

        return CommentMapper.toDto(updatedComment);
    }

    @Transactional
    public void deleteComment(String commentId) {
        log.info("Deleting comment with ID: " + commentId);
        if (!commentRepository.existsById(commentId)) {
            log.error("Comment not found for deletion, ID: " + commentId);
            throw new CommentNotFoundException(commentId);
        }
        commentRepository.deleteById(commentId);
        log.info("Comment deleted successfully, ID: " + commentId);
    }

    @Transactional
    public VoteResponseDto voteComment(String commentId, VoteRequestDto voteRequest) {
        log.info("Voting on comment, ID: " + commentId + ", vote type: " + voteRequest.getVoteType());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

//        hardcoded - current_user
        User user = userRepository.findByUsername("current_user")
                .orElseThrow(() -> new UserNotFoundException("User not found: current_user"));

        voteService.vote(user, null, comment, voteRequest.getVoteType());

        comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        return VoteMapper.toDto(comment, user.getUsername());
    }

}
