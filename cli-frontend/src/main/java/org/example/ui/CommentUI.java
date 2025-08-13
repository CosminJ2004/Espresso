package org.example.ui;

import org.example.models.Comment;
import org.example.textlib.IndentPlacer;

public class CommentUI {
    private static CommentUI instance;

    private CommentUI() {}

    public static CommentUI getInstance() {
        if (instance == null) {
            instance = new CommentUI();
        }
        return instance;
    }

    public void renderComment(Comment comment, int indentLevel) {
        System.out.println(IndentPlacer.place(indentLevel) + comment.getAuthor());
        System.out.println(IndentPlacer.place(indentLevel) + comment.getContent());
    }

}
