package model;

public class Vote {
    private User user;
    private VoteType type;

    public Vote(User user, VoteType type) {
        this.user = user;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public VoteType getType() {
        return type;
    }

    public void setType(VoteType type) {
        this.type = type;
    }
}
