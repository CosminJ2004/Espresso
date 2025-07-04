import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private static int counter;
    private int id;
    private String author;
    private String content;
    private List<Comment> commentList = new ArrayList<>();
    private int votes;

    public Post(String author, String content) {
        this.id = ++counter;
        this.author = author;
        this.content = content;
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
        String result = "[" + id + "] " + content + " (by " + author + ") Votes: " + votes;
        if (votes >= 10) {
            result += " ⭐";
        }
        return result;
    }
}
