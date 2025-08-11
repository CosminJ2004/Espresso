package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public void vote(User user, Post post, Comment comment, VoteType voteType) {
        Optional<Vote> existingVote;

        if (post != null) {
            existingVote = voteRepository.findByUserAndPost(user, post);
        } else if (comment != null) {
            existingVote = voteRepository.findByUserAndComment(user, comment);
        } else {
            throw new IllegalArgumentException("Either post or comment must be provided");
        }

        if (voteType == null || VoteType.NONE == voteType) {
            existingVote.ifPresent(voteRepository::delete);
        } else {
            if (existingVote.isPresent()) {
                Vote vote = existingVote.get();
                vote.setType(voteType);
                voteRepository.save(vote);
            } else {
                Vote newVote;
                if (post != null) {
                    newVote = new Vote(user, post, voteType);
                } else {
                    newVote = new Vote(user, comment, voteType);
                }
                voteRepository.save(newVote);
            }
        }
    }
}
