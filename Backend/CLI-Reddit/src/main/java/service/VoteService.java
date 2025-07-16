package service;

import model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VoteService {
    private static final VoteService instance = new VoteService();
    private final Map<Votable, Map<User, Vote>> voteData = new HashMap<>();     // Voturile pentru orice Votable (Post sau Comment)
    private final Map<Votable, Integer> voteCounts = new HashMap<>();
    private final Scanner scanner;

    private VoteService() {
        this.scanner = new Scanner(System.in);
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
            return false;
        }

        if (existingVote != null) {
            // schimbare vot
            if (voteType == VoteType.UPVOTE) {
                currentVotes += 2; // de la down la up
            } else {
                currentVotes -= 2; // de la up la down
            }
            existingVote.setType(voteType);
        } else {
            // vot nou
            if (voteType == VoteType.UPVOTE) {
                currentVotes += 1;
            } else {
                currentVotes -= 1;
            }
            userVotes.put(user, new Vote(user, voteType));
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
    }

    public boolean upvoteToPost() {
        System.out.print("Enter post ID to upvote: ");
        int postId = Integer.parseInt(scanner.nextLine());
        PostService postService = PostService.getInstance();
        Post post = postService.getPostById(postId);
        if (post == null) {
            System.out.println("Post not found.");
            return false;
        }
        return vote(post, UserService.getCurrentUser(), VoteType.UPVOTE);
    }

    public boolean downvoteToPost() {
        System.out.print("Enter post ID to downvote: ");
        int postId = Integer.parseInt(scanner.nextLine());
        PostService postService = PostService.getInstance();
        Post post = postService.getPostById(postId);
        if (post == null) {
            System.out.println("Post not found.");
            return false;
        }
        return vote(post, UserService.getCurrentUser(), VoteType.DOWNVOTE);
    }

    public boolean upVoteToComment() {
        System.out.print("Enter comment ID to upvote: ");
        int commentId = Integer.parseInt(scanner.nextLine());
        CommentService commentService = CommentService.getInstance();
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            System.out.println("Comment not found.");
            return false;
        }
        return vote(comment, UserService.getCurrentUser(), VoteType.UPVOTE);
    }

    public boolean downVoteToComment() {
        System.out.print("Enter comment ID to downvote: ");
        int commentId = Integer.parseInt(scanner.nextLine());
        CommentService commentService = CommentService.getInstance();
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            System.out.println("Comment not found.");
            return false;
        }
        return vote(comment, UserService.getCurrentUser(), VoteType.DOWNVOTE);
    }
}
