import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private static int counter;
    private int id;
    private String author;
    private String summary;
    private String content;
    private List<CommentPost> commentList = new ArrayList<>();
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

    public void addComment(CommentPost comment) {
        commentList.add(comment);
    }

    public List<CommentPost> getComments() {
        return commentList;
    }

    public void showAllComments()
    {
        for(CommentPost commentPost: commentList)
        {
            commentPost.showComment();//TO DO adding indentation logic with levels
            commentPost.showReplies();//same as reddit
            //need to see a comment id also to have better debugging
        }
    }

    public String display() {
        String result = "[" + id + "] " + summary  + " (by " + author + ") Votes: " + votes;
        if (votes >= 10) {
            result += " ⭐";
        }
        return result;
    }

    public void expand() {
        String result = "[" + id + "] " + summary +" " +content + " (by " + author + ") Votes: " + votes;
        if (votes >= 10) {
            result += " ⭐";
        }
        System.out.println(result);
        for (CommentPost comment : commentList)
            comment.display(1);
    }
}
