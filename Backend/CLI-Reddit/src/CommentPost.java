import java.util.ArrayList;
import java.util.List;

public class CommentPost extends Comment {
    private Post post;
    public List<CommentCom> replies = new ArrayList<>();


    public CommentPost(User user, String textComment,Post post)
    {
        super(user,textComment);
        this.post=post;
    }
    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }
    public void addReply(CommentCom com)
    {
        replies.add(com);
    }
    public void showReplies()
    {
        for (CommentCom repl:replies)
        {//all comments of a certain commm
            repl.display();
        }
    }
    public String getTextComment() {
        return textComment;
    }
    public void showComment()
    {
        System.out.println(post.getSummary());
        System.out.println(user.getUsername());
        System.out.println(textComment);

    }

    public void display() {
        System.out.println(" [" + id + "] " + textComment + " (by " + user.getUsername() + ")");
        showReplies();
    }
}
