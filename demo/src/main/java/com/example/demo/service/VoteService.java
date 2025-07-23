package com.example.demo.service;

import com.example.demo.dto.VoteDto;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Vote vote(VoteDto dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        if (userOpt.isEmpty()) throw new RuntimeException("User not found");

        User user = userOpt.get();

        Vote vote;

        if (dto.getPostId() != null) {
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found"));
            vote = new Vote(user, post, dto.getType());
        } else if (dto.getCommentId() != null) {
            Comment comment = commentRepository.findById(dto.getCommentId())
                    .orElseThrow(() -> new RuntimeException("Comment not found"));
            vote = new Vote(user, comment, dto.getType());
        } else {
            throw new RuntimeException("Neither postId nor commentId provided");
        }

        return voteRepository.save(vote);
    }

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public void deleteVote(Long voteId) {
        voteRepository.deleteById(voteId);
    }

    public Vote getVote(Long id) {
        return voteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vote not found"));
    }
}
