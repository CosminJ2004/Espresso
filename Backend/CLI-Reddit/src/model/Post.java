package model;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Post implements IEntity {

    private static final int MIN_VOTES_FOR_STAR = 10;
    private static int counter;
    private int id;
    private String author;
    private String summary;
    private String content;
    private List<CommentPost> commentList = new ArrayList<>();
    private Map<String, String> userVotes = new HashMap<>();
    private int votes;
    private LocalDateTime dateTime;

    public Post(String author, String summary, String content) {
        this.id = ++counter;
        this.author = author;
        this.summary = summary;
        this.content = content;
        this.dateTime = LocalDateTime.now();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = dateTime.format(formatter);
        String result = "[" + id + "] " + summary + " (by " + author + ") Votes: " + votes + " | Posted on: " + formattedDateTime;
        if (votes >= MIN_VOTES_FOR_STAR) {
            result += " ⭐";
        }
        return result;
    }

    public void expand() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = dateTime.format(formatter);
        String result = "[" + id + "] " + summary + " " + content + " (by " + author + ") Votes: " + votes+ " | Posted on: " + formattedDateTime;
        if (votes >= MIN_VOTES_FOR_STAR) {
            result += " ⭐";
        }
        System.out.println(result);
        if (commentList.isEmpty()) {
            System.out.println("There are no comments");
        } else {
            for (CommentPost comment : commentList)
                comment.display(1);
        }

    }

//    public model.CommentPost getCommentPostById(int commentId) {
//        for (int i = 0; i < commentList.size(); i++) {
//            if (commentList.get(i).getId() == commentId) {
//                return commentList.get(i);
//            }
//        }
//
//        System.out.println("Comment not found.");
//        return null;
//    }
}
