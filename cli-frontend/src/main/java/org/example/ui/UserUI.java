package org.example.ui;

public class UserUI {
    private static UserUI instance;

    private UserUI() {}

    public static UserUI getInstance() {
        if (instance == null) {
            instance = new UserUI();
        }
        return instance;
    }
}
