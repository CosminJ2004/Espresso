package service;

import model.*;
import logger.*;
import util.Console;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Scanner;

public class CommentService {
    private static  CommentService instance = new CommentService();
    private static final Map<Integer, Comment> allComments = new HashMap<>();
    private final PostService postService;


    private CommentService() {
        this.postService = PostService.getInstance();

        Log.log(LogLevel.INFO, "CommentService initialized.");
    }

    public static CommentService getInstance() {
        return instance;
    }

    public Comment addComment(User user, String text, Post post, Comment parent) {
        Comment comment = new Comment(user, text, post, parent);
        //creating the comment
        allComments.put(comment.getId(), comment);

        if (parent == null) {
            //if the parrent is null -> comment to a post
            postService.addComment(post, comment);
            Log.log(LogLevel.INFO, "Comment ID " + comment.getId() + " added to post ID " + post.getId());
        } else { //otherwise it is a reply
            this.addReply(parent, comment);
            Log.log(LogLevel.INFO, "Reply ID " + comment.getId() + " added to parent comment ID " + parent.getId());
        }

        return comment;
    }

    public Comment getById(int id) {
        Comment comment = allComments.get(id);
        if (comment == null) {
            Log.log(LogLevel.WARN, "Tried to fetch comment with ID " + id + " but it does not exist.");
        } else {
            Log.log(LogLevel.INFO, "Fetched comment with ID " + id);
        }
        return comment;
    }

    public void addReply(Comment parent, Comment reply) {
        parent.getReplies().add(reply);
        Log.log(LogLevel.INFO, "Reply ID " + reply.getId() + " added to comment ID " + parent.getId());
    }

    public void displayComment(Comment comment, int level) {
        VoteService voteService = VoteService.getInstance();
        int votes = voteService.getVoteCount(comment);
        String indent = " ".repeat(level * 2);
        System.out.println(indent + comment.getText() + " written by " + comment.getAuthor().getUsername()
                + " Comment ID: " + comment.getId() + " Votes: " + votes + " | Posted on: " + comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        Log.log(LogLevel.INFO, "Displayed comment ID " + comment.getId() + " with " + votes + " votes");

        for (Comment reply : comment.getReplies()) {
            displayComment(reply, level + 1);
        }
    }

    public void addCommentToPost(Post post) {


        String text = Console.readText("Write your comment: ");
        Comment comment = addComment(UserService.getCurrentUser(), text, post, null);

        Log.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
                " added comment ID " + comment.getId() + " to post ID " + post.getId());
    }

    public void addCommentToComment() {

        int commentId=Console.readInt("Enter comment ID to comment: ");

        Comment comment = getById(commentId);
        if (comment == null) {
            System.out.println("Comment not found.");
            Log.log(LogLevel.ERROR, "User attempted to reply to non-existent comment ID " + commentId);
            return;
        }

        String text = Console.readText("Write your comment: ");
        Post currentPost = comment.getPost();

        Comment newComment = addComment(UserService.getCurrentUser(), text, currentPost, comment);

        Log.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
                " added reply comment ID " + newComment.getId() + " to comment ID " + commentId);
    }

    public static void resetInstance() {
        instance = null;
    }
}
