package content;

import user.User;

import java.util.ArrayList;
import java.util.List;

public class CommentPost extends Comment implements IEntity {
    private Post post;
    public List<CommentCom> replies = new ArrayList<>();


    public CommentPost(User user, String textComment, Post post)
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
            repl.display(1);
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


    public void display(int indentLevel) {
        String indent = "  ".repeat(indentLevel);
        System.out.println(indent + "[" + id + "] " + textComment + " (by " + user.getUsername() + ") Votes: " + getVotes());
        for (CommentCom repl : replies) {
            repl.display(indentLevel + 1);
        }
    }
    //TO DO
    @Override
    public boolean upvote(String username) {
        return super.upvote(username);
    }

    @Override
    public boolean downvote(String username) {
        return super.downvote(username);
    }

    @Override
    public void expand() {

    }
}
