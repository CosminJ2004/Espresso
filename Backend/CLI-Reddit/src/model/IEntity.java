package model;

public interface IEntity {
    public boolean upvote(String username);
    public boolean downvote(String username);
//    public boolean getInfoAboutUser();
    // nice to have
    public void expand();

}
