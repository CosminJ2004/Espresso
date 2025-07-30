package service;

import model.*;
import logger.*;
import util.Console;

import java.util.HashMap;
import java.util.Map;


public class VoteService {
    private static VoteService instance = new VoteService();
    private final Map<Votable, Map<User, Vote>> voteData = new HashMap<>();
    private final Map<Votable, Integer> voteCounts = new HashMap<>();


    private VoteService() {

    }

    public static VoteService getInstance() {
        return instance;
    }

    public boolean vote(Votable votable, User user, VoteType voteType) {
        Map<User, Vote> userVotes = voteData.computeIfAbsent(votable, k -> new HashMap<>());
        Vote existingVote = userVotes.get(user);
        int currentVotes = voteCounts.getOrDefault(votable, 0);

        if (existingVote != null && existingVote.getType() == voteType) {
            System.out.println("You already voted this way.");
            Log.log(LogLevel.INFO, "User " + user.getUsername() + " attempted to " + voteType +
                    " on ID " + votable.getId() + " again (no change).");
            return false;
        }

        if (existingVote != null) {
            // schimbare vot
            if (voteType == VoteType.UPVOTE) {
                currentVotes += 2;
            } else {
                currentVotes -= 2;
            }
            Log.log(LogLevel.INFO, "User " + user.getUsername() + " changed vote from " +
                    existingVote.getType() + " to " + voteType + " on ID " + votable.getId());
            existingVote.setType(voteType);
        } else {
            // vot nou
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
        Log.log(LogLevel.INFO, "Votes cleared for ID " + votable.getId());
    }

    public void printVotes(Votable votable) {
        Map<User, Vote> userVotes = voteData.get(votable);
        if (userVotes == null || userVotes.isEmpty()) {
            System.out.println("No votes for id [" + votable.getId() + "]");
            return;
        }

        System.out.println("Votes for id [" + votable.getId() + "]:");
        for (Map.Entry<User, Vote> entry : userVotes.entrySet()) {
            System.out.println("User: " + entry.getKey().getUsername() + " - " + entry.getValue().getType());
        }
        Log.log(LogLevel.INFO, "Displayed votes for ID " + votable.getId());
    }

    public boolean upvoteToPost(Post post) {

        boolean success = vote(post, UserService.getCurrentUser(), VoteType.UPVOTE);
        if (success) {
            Log.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
                    " upvoted post ID " + post.getId());
        }
        return success;
    }

    public boolean downvoteToPost(Post post) {

        boolean success = vote(post, UserService.getCurrentUser(), VoteType.DOWNVOTE);
        if (success) {
            Log.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
                    " downvoted post ID " + post.getId());
        }
        return success;
    }

    public boolean upVoteToComment() {

        int commentId = Console.readInt("Enter comment ID to upvote");
        CommentService commentService = CommentService.getInstance();
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            System.out.println("Comment not found.");
            Log.log(LogLevel.WARN, "Attempted upvote on non-existent comment ID " + commentId);
            return false;
        }
        boolean success = vote(comment, UserService.getCurrentUser(), VoteType.UPVOTE);
        if (success) {
            Log.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
                    " upvoted comment ID " + commentId);
        }
        return success;
    }

    public boolean downVoteToComment() {

        int commentId = Console.readInt("Enter comment ID to downvote: ");
        CommentService commentService = CommentService.getInstance();
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            System.out.println("Comment not found.");
            Log.log(LogLevel.WARN, "Attempted downvote on non-existent comment ID " + commentId);
            return false;
        }
        boolean success = vote(comment, UserService.getCurrentUser(), VoteType.DOWNVOTE);
        if (success) {
            Log.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
                    " downvoted comment ID " + commentId);
        }
        return success;
    }
    public static void resetInstance() {
        instance = null;
    }
}
