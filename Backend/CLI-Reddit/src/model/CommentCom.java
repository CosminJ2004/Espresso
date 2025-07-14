package model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class CommentCom extends Comment implements IEntity {
    private Comment comment;
    public List<CommentCom> repliesCom = new ArrayList<>();
    private LocalDateTime dateTime;

    public CommentCom(User user, String textComment)
    {

        super(user,textComment);
        this.dateTime = LocalDateTime.now();
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }
    public void addReply(CommentCom com)
    {
        repliesCom.add(com);
    }
    public void showReplies()
    {
        for (CommentCom repl:repliesCom)
        {//all comments of a certain commm
            repl.display(0);
        }
    }
    public String getTextComment() {
        return textComment;
    }
    public void showComment()
    {
        System.out.println(user.getUsername());
        System.out.println(textComment);
    }

    public void display(int indentLevel) {
        String indent = "  ".repeat(indentLevel);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = dateTime.format(formatter);
        System.out.println(indent + "[" + id + "] " + textComment + " (by " + user.getUsername() + ") Votes: " + getVotes()+ " | Posted on: " + formattedDateTime);
        for (CommentCom repl : repliesCom) {
            repl.display(indentLevel + 1);
        }
    }

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
