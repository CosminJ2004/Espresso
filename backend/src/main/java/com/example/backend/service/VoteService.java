package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.repository.*;
import com.example.backend.util.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final Logger log;

    @Autowired
    public VoteService(VoteRepository voteRepository, Logger log) {
        this.voteRepository = voteRepository;
        this.log = log;
    }

    @Transactional
    public void vote(User user, Post post, Comment comment, VoteType voteType) {
        String targetType = post != null ? "post" : "comment";
        String targetId = post != null ? post.getId() : comment.getId();
        log.info("Processing vote for " + targetType + " ID: " + targetId + ", user: " + user.getUsername() + ", vote type: " + voteType);
        
        Optional<Vote> existingVote;

        if (post != null) {
            existingVote = voteRepository.findByUserAndPost(user, post);
        } else {
            existingVote = voteRepository.findByUserAndComment(user, comment);
        }

        if (voteType == null || VoteType.NONE == voteType) {
            if (existingVote.isPresent()) {
                log.info("Removing existing vote for " + targetType + " ID: " + targetId);
                voteRepository.delete(existingVote.get());
                voteRepository.flush();
            }
        } else {
            if (existingVote.isPresent()) {
                Vote vote = existingVote.get();
                if (vote.getType().equals(voteType)) {
                    log.info("Removing existing vote for " + targetType + " ID: " + targetId + " (same vote type submitted)");
                    voteRepository.delete(vote);
                    voteRepository.flush();
                } else {
                    log.info("Updating existing vote for " + targetType + " ID: " + targetId + " to " + voteType);
                    vote.setType(voteType);
                    voteRepository.save(vote);
                    voteRepository.flush();
                }
            } else {
                log.info("Creating new vote for " + targetType + " ID: " + targetId + " with type " + voteType);
                Vote newVote;
                if (post != null) {
                    newVote = new Vote(user, post, voteType);
                } else {
                    newVote = new Vote(user, comment, voteType);
                }
                voteRepository.save(newVote);
            }
        }
        
        log.info("Vote processing completed for " + targetType + " ID: " + targetId);
    }
}
