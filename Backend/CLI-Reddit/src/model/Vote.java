package model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote)) return false;
        Vote vote = (Vote) o;
        return Objects.equals(user, vote.user) && type == vote.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, type);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "user='" + user.getUsername() + '\'' +
                ", type=" + type +
                '}';
    }
}
