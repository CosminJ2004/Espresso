package org.example.ui;

public class PostUI {
    private static PostUI instance;
    private static final String PLEASE_ENTER = "Please enter ";

    public static PostUI getInstance() {
        if (instance == null) {
            instance = new PostUI();
        }
        return instance;
    }

    public static String pleaseEnter(String choice) {
        return PLEASE_ENTER + choice + ":";
    }

}
