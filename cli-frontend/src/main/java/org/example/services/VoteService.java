package org.example.services;

public class VoteService {
    private static VoteService instance;

    private VoteService() {}

    public static VoteService getInstance() {
        if (instance == null) {
            instance = new VoteService();
        }
        return instance;
    }
}
