package com.example.demo.service;
import com.example.demo.model.*;
import com.example.demo.util.Console;
import com.example.demo.util.logger.Log;
import com.example.demo.util.logger.LogLevel;
import org.springframework.stereotype.Service;
//import util.Console;

import java.util.HashMap;
import java.util.Map;

@Service
public class VoteService {

    // Using a Map to keep track of votes, same as your original logic
    private final Map<Votable, Map<Users, Vote>> voteData = new HashMap<>();
    private final Map<Votable, Integer> voteCounts = new HashMap<>();

    // No static instance; Spring manages the bean lifecycle

    public boolean vote(Votable votable, Users user, VoteType voteType) {
        Map<Users, Vote> userVotes = voteData.computeIfAbsent(votable, k -> new HashMap<>());
        Vote existingVote = userVotes.get(user);
        int currentVotes = voteCounts.getOrDefault(votable, 0);

        if (existingVote != null && existingVote.getType() == voteType) {
            System.out.println("You already voted this way.");
            Log.log(LogLevel.INFO, "User " + user.getUsername() + " attempted to " + voteType +
                    " on ID " + votable.getId() + " again (no change).");
            return false;
        }

        if (existingVote != null) {
            // change vote
            if (voteType == VoteType.UPVOTE) {
                currentVotes += 2;
            } else {
                currentVotes -= 2;
            }
            Log.log(LogLevel.INFO, "User " + user.getUsername() + " changed vote from " +
                    existingVote.getType() + " to " + voteType + " on ID " + votable.getId());
            existingVote.setType(voteType);
        } else {
            // new vote
            if (voteType == VoteType.UPVOTE) {
                currentVotes += 1;
            } else {
                currentVotes -= 1;
            }
            userVotes.put(user, new Vote(user, voteType));
            Log.log(LogLevel.INFO, "User " + user.getUsername() + " cast " + voteType +
                    " on ID " + votable.getId());
        }

        voteCounts.put(votable, currentVotes);
        return true;
    }

    public int getVoteCount(Votable votable) {
        return voteCounts.getOrDefault(votable, 0);
    }

    public void clearVotes(Votable votable) {
        voteData.remove(votable);
        voteCounts.remove(votable);
//        Log.log(LogLevel.INFO, "Votes cleared for ID " + votable.getId());
    }

    public void printVotes(Votable votable) {
        Map<Users, Vote> userVotes = voteData.get(votable);
        if (userVotes == null || userVotes.isEmpty()) {
            System.out.println("No votes for id [" + votable.getId() + "]");
            return;
        }

        System.out.println("Votes for id [" + votable.getId() + "]:");
        for (Map.Entry<Users, Vote> entry : userVotes.entrySet()) {
            System.out.println("User: " + entry.getKey().getUsername() + " - " + entry.getValue().getType());
        }
        Log.log(LogLevel.INFO, "Displayed votes for ID " + votable.getId());
    }

    // Assuming you have UserService as a Spring bean and accessible here via DI
    // For this example, let's suppose you inject UserService here

    private final UserService userService;
    private final CommentService commentService;

    public VoteService(UserService userService, CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }

    public boolean upvoteToPost(Post post) {
        Users currentUser = userService.getCurrentUser();
        boolean success = vote(post, currentUser, VoteType.UPVOTE);
        if (success) {
            Log.log(LogLevel.INFO, "User " + currentUser.getUsername() +
                    " upvoted post ID " + post.getId());
        }
        return success;
    }

    public boolean downvoteToPost(Post post) {
        Users currentUser = userService.getCurrentUser();
        boolean success = vote(post, currentUser, VoteType.DOWNVOTE);
        if (success) {
//            Log.log(LogLevel.INFO, "User " + currentUser.getUsername() +
//                    " downvoted post ID " + post.getId());
        }
        return success;
    }

    public boolean upVoteToComment() {
        int commentId = Console.readInt("Enter comment ID to upvote");
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            System.out.println("Comment not found.");
            Log.log(LogLevel.WARN, "Attempted upvote on non-existent comment ID " + commentId);
            return false;
        }
        Users currentUser = userService.getCurrentUser();
        boolean success = vote(comment, currentUser, VoteType.UPVOTE);
        if (success) {
            Log.log(LogLevel.INFO, "User " + currentUser.getUsername() +
                    " upvoted comment ID " + commentId);
        }
        return success;
    }

    public boolean downVoteToComment() {
        int commentId = Console.readInt("Enter comment ID to downvote: ");
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            System.out.println("Comment not found.");
            Log.log(LogLevel.WARN, "Attempted downvote on non-existent comment ID " + commentId);
            return false;
        }
        Users currentUser = userService.getCurrentUser();
        boolean success = vote(comment, currentUser, VoteType.DOWNVOTE);
        if (success) {
            Log.log(LogLevel.INFO, "User " + currentUser.getUsername() +
                    " downvoted comment ID " + commentId);
        }
        return success;
    }
}
