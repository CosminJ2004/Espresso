package service;

import model.*;

import java.util.HashMap;
import java.util.Map;

public class VoteService {

    // Voturile pentru orice Votable (Post sau Comment)
    private final Map<Votable, Map<User, Vote>> voteData = new HashMap<>();

    private final Map<Votable, Integer> voteCounts = new HashMap<>();

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
}
