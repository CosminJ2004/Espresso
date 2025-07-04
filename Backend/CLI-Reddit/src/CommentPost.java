public class CommentPost extends Comment {
    private Post post;

    public CommentPost(User user, String textComment,Post post)
    {
        super(user,textComment);
        this.post=post;
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
