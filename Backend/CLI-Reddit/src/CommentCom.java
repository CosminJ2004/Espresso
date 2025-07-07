public class CommentCom extends Comment {
    private Comment comment;

    public CommentCom(User user, String textComment)
    {
        super(user,textComment);
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }

    public String getTextComment() {
        return textComment;
    }
    public void showComment()
    {
        System.out.println(user.getUsername());
        System.out.println(textComment);
    }

    public void display() {
        System.out.println("  [" + id + "] " + textComment + " (by " + user.getUsername() + ")");
    }
}
