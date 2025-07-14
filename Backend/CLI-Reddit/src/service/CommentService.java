package service;

import model.*;

import java.util.*;

public class CommentService {
    private final Map<Integer, Comment> allComments = new HashMap<>();
     PostService postService=new PostService();


    public Comment addComment(User user, String text, Post post, Comment parent) {
        Comment comment = new Comment(user, text, post, parent);
        allComments.put(comment.getId(), comment);

        if (parent == null) {
            postService.addComment(post,comment);
        } else {
            parent.addReply(comment);
        }
        return comment;
    }

    public Comment getById(int id) {
        return allComments.get(id);
    }
}
