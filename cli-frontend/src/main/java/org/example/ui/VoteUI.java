package org.example.ui;

public class VoteUI {
    private static VoteUI instance;

    private VoteUI() {}

    public static VoteUI getInstance() {
        if(instance == null) {
            instance = new VoteUI();
        }
        return instance;
    }
}
