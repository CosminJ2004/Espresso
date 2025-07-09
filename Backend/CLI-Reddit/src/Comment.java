import java.util.HashMap;
import java.util.Map;

public abstract class Comment {
    protected User user;
    protected String textComment;
    protected int id;
    private static int counter=0;
    private Map<String, String> comVotes = new HashMap<>();
    private int votes;

    public int getVotes() {
        return votes;
    }

    public Comment(User user, String textComment)
    {

        this.user=user;
        this.textComment=textComment;
        this.id=++counter;
    }

    public int getId() {
        return id;
    }


    public boolean upvote(String username) {
        String previousVote = comVotes.get(username);
        if ("upvote".equals(previousVote)) {
            System.out.println("You already voted this post.");
            return false;
        }
        if ("downvote".equals(previousVote)) {
            votes += 2;
        } else {
            votes += 1;
        }
        comVotes.put(username, "upvote");
        return true;
    }

    public boolean downvote(String username) {
        String previousVote = comVotes.get(username);
        if ("downvote".equals(previousVote)) {
            System.out.println("You already voted this post.");
            return false;
        }
        if ("upvote".equals(previousVote)) {
            votes -= 2;
        } else {
            votes -= 1;
        }

        comVotes.put(username, "downvote");
        return true;
    }

}
