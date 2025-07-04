public abstract class Comment {
    protected User user;
    protected String textComment;
    protected int id;
    private static int counter=0;

    public Comment(User user, String textComment)
    {
        this.user=user;
        this.textComment=textComment;
        this.id=++counter;
    }

    public int getId() {
        return id;
    }
}
