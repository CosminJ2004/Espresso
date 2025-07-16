package service;

import model.*;
import java.util.*;

public class CommentService {
    private static final CommentService instance = new CommentService();
    private static final Map<Integer, Comment> allComments = new HashMap<>();
    private final PostService postService;

    private CommentService() {
        this.postService = PostService.getInstance();
    }

    public static CommentService getInstance() {
        return instance;
    }

    public Comment addComment(User user, String text, Post post, Comment parent) {
        Comment comment = new Comment(user, text, post, parent);
        allComments.put(comment.getId(), comment);

        if (parent == null) {
            System.out.println("e comment la post");
            postService.addComment(post,comment);

        } else {
            this.addReply(parent, comment);
            System.out.println("e comment la com");
        }
        return comment;
    }

    public Comment getById(int id) {
        return allComments.get(id);
    }

    public void addReply(Comment parent, Comment reply) {
        parent.getReplies().add(reply);
    }

    public boolean upvoteComment(Comment comment, String username) {
        String prev = comment.getUserVotes().get(username);
        if ("upvote".equals(prev)) return false;
        if ("downvote".equals(prev)) comment.setVotes(comment.getVotes() + 2);
        else comment.setVotes(comment.getVotes() + 1);
        comment.getUserVotes().put(username, "upvote");
        return true;
    }

    public boolean downvoteComment(Comment comment, String username) {
        String prev = comment.getUserVotes().get(username);
        if ("downvote".equals(prev)) return false;
        if ("upvote".equals(prev)) comment.setVotes(comment.getVotes() - 2);
        else comment.setVotes(comment.getVotes() - 1);
        comment.getUserVotes().put(username, "downvote");
        return true;
    }

    public void displayComment(Comment comment, int level) {
        VoteService voteService = VoteService.getInstance();
        int votes = voteService.getVoteCount(comment);
        // indentare în funcție de level
        String indent = " ".repeat(level * 2);
        System.out.println(indent + "Comment ID: " + comment.getId() + " Votes: " + votes);
        System.out.println(indent + comment.getText());

        for (Comment reply : comment.getReplies()) {
            displayComment(reply, level + 1);
        }
    }
}
