import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private static int counter;
    private int id;
    private String author;
    private String summary;
    private String content;
    private List<Comment> commentList = new ArrayList<>();
    private int votes;

    public Post(String author, String summary, String content) {
        this.id = ++counter;
        this.author = author;
        this.summary =summary;
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

    public void upvote() {
        votes++;
    }

    public void downvote() {
        votes--;
    }

    public int getVotes() {
        return votes;
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public List<Comment> getComments() {
        return commentList;
    }

    public String display() {
        String result = "[" + id + "] " + summary + " (by " + author + ") Votes: " + votes;
        if (votes >= 10) {
            result += " ⭐";
        }
        return result;
    }
    public String expand(){
        String result = "[" + id + "] " + summary + content + " (by " + author + ") Votes: " + votes;
        if (votes >= 10) {
            result += " ⭐";
        }
        return result;
    }
}
