package org.example.services;

public class CommentService {
    private static CommentService instance;

    private CommentService() {}

    public static CommentService getInstance() {
        if (instance == null) {
            instance = new CommentService();
        }
        return instance;
    }


}
