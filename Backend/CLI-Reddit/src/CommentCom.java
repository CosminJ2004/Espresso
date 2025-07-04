public class CommentCom extends Comment {
    private Comment comment;

    public CommentCom(User user, String textComment,Comment comment)
    {
        super(user,textComment);
        this.comment=comment;
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
}
