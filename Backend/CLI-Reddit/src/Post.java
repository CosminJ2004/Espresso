import javax.xml.stream.events.Comment;
import java.util.*;

public class Post {
    private static final int MIN_VOTES_FOR_STAR=10;
    private static int counter;
    private int id;
    private String author;
    private String summary;
    private String content;
    private List<CommentPost> commentList = new ArrayList<>();
    private Map<String, String> userVotes = new HashMap<>();

    private int votes;

    public Post(String author, String summary, String content) {
        this.id = ++counter;
        this.author = author;
        this.summary = summary;
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public boolean upvote(String username) {
        String previousVote = userVotes.get(username);
        if ("upvote".equals(previousVote)) {
            System.out.println("You already voted this post.");
            return false;
        }
        if ("downvote".equals(previousVote)) {
            votes += 2;
        } else {
            votes += 1;
        }
        userVotes.put(username, "upvote");
        return true;
    }

    public boolean downvote(String username) {
        String previousVote = userVotes.get(username);
        if ("downvote".equals(previousVote)) {
            System.out.println("You already voted this post.");
            return false;
        }
        if ("upvote".equals(previousVote)) {
            votes -= 2;
        } else {
            votes -= 1;
        }

        userVotes.put(username, "downvote");
        return true;
    }

    public int getVotes() {
        return votes;
    }

    public void addComment(CommentPost comment) {
        commentList.add(comment);
    }

    public List<CommentPost> getComments() {
        return commentList;
    }


    public void showAllComments() {
        for (CommentPost commentPost : commentList) {
            commentPost.showComment();//TO DO adding indentation logic with levels
            commentPost.showReplies();//same as reddit
            //need to see a comment id also to have better debugging
        }
    }

    public String display() {
        String result = "[" + id + "] " + summary + " (by " + author + ") Votes: " + votes;
        if (votes >= MIN_VOTES_FOR_STAR) {
            result += " ⭐";
        }
        return result;
    }

    public void expand() {
        String result = "[" + id + "] " + summary + " " + content + " (by " + author + ") Votes: " + votes;
        if (votes >= MIN_VOTES_FOR_STAR) {
            result += " ⭐";
        }
        System.out.println(result);
        for (CommentPost comment : commentList)
            comment.display();
    }

    public CommentPost getCommentPostById(int commentId) {
        for (int i = 0; i < commentList.size(); i++) {
            if (commentList.get(i).getId() == commentId) {
                return commentList.get(i);
            }
        }

        System.out.println("Comment not found.");
        return null;
    }
}
