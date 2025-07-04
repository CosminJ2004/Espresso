public abstract class Comment {
    protected User user;
    protected String textComment;
    protected int id;
    protected int upvotes;
    private static int counter=0;

    public Comment(User user, String textComment)
    {
        this.upvotes=0;
        this.user=user;
        this.textComment=textComment;
        this.id=++counter;
    }

    public int getId() {
        return id;
    }
    public void upvoteComment()
    {
        this.upvotes++;
    }

}
