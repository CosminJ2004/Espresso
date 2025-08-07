package org.example.ui;

public class CommentUI {
    private static CommentUI instance;

    private CommentUI() {}

    public static CommentUI getInstance() {
        if (instance == null) {
            instance = new CommentUI();
        }
        return instance;
    }

}
