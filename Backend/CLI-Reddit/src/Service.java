import java.util.*;

public class Service {

    List<Post> posts = new ArrayList<>();
    List<CommentPost> commentPosts = new ArrayList<>();
    List<CommentCom> commentComs = new ArrayList<>();
    List<Comment> commentsAll=new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    UserService userService=new UserService();




    private int currentPostID;
    private Post currentPost;

    public boolean login() {
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        userService.login(username, password);
        return UserContext.isLoggedIn();
    }

    public boolean register() {
        System.out.println("Please enter the desired username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter the desired password: ");
        String password = scanner.nextLine();
        userService.register(username, password);
        return UserContext.isLoggedIn() && userService.login(username, password);
    }

    public void createPost() {
        System.out.println("Enter the summary of the post:");
        String summary = scanner.nextLine();
        System.out.println("Enter the content of the post:");
        String content = scanner.nextLine();
        Post post = new Post(UserContext.getCurrentUser().getUsername(), summary, content);
        posts.add(post);
    }

    public void showPosts() {
        for (Post post : posts) {
            String msg = post.display();
            System.out.println(msg);
        }
    }

    public Post getPostById(int postID) {
        for (Post post : posts) {
            if (post.getId() == postID) {
                return post;
            }
        }
        System.out.println("Post not found.");
        return null;
    }

    public void openPost() {
        System.out.println("Choose the Post Id you wish to open:");
        currentPostID = scanner.nextInt();
        scanner.nextLine();
        currentPost = getPostById(currentPostID);
    }

    public void expandPost() {
        currentPost.expand();
    }

    public void deletePost() {
        if (currentPost.getAuthor().equals(UserContext.getCurrentUser().getUsername())) {
            posts.remove(currentPost);
            System.out.println("Post deleted successfully.");
        } else {
            System.out.println("You can only delete your own posts.");
        }
    }

    public void addCommentToPost() {
        System.out.println("Write your comment:");
        String textComment = scanner.nextLine();
        //casting and adding them to the list of comments of posts
        CommentPost commentPost = new CommentPost(UserContext.getCurrentUser(), textComment, currentPost);
//        commentPosts.add(commentPost);//adding also in a list
        currentPost.addComment(commentPost);//adding comments to a post object
        commentsAll.add(commentPost); //adding the comment to the list pf all comments
    }

    public void addCommentToComment() {
        System.out.println("Choose the Comment Id you wish to comment on:");
        int commentId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Write your comment:");
        String textComment = scanner.nextLine();

        CommentCom commentCom = new CommentCom(UserContext.getCurrentUser(), textComment); // creating the comment

        boolean found = false;
        Comment currentComment = null;

        for (int i = 0; i < commentsAll.size(); i++) {
            if (commentsAll.get(i).getId() == commentId) {
                currentComment = commentsAll.get(i);
                found = true;
                break;
            }
        }

        if (!found || currentComment == null) {
            System.out.println("Comment not found.");
            return;
        }

        // Cast & Add reply
        if (currentComment instanceof CommentPost) {
            CommentPost cp = (CommentPost) currentComment;
            cp.addReply(commentCom); // add as reply
//            cp.showComment();

        } else if (currentComment instanceof CommentCom) {
            CommentCom cc = (CommentCom) currentComment;
            cc.addReply(commentCom); // add as reply
//            cc.showReplies();

        }
        commentsAll.add(commentCom);

    }
    public void upVoteToPost() {


        if (currentPost.upvote(UserContext.getCurrentUser().getUsername())) {
            System.out.println("Post upvoted successfully!");
        }
    }

    public void downVoteToPost() {
        if (currentPost.downvote(UserContext.getCurrentUser().getUsername())) {
            System.out.println("Post upvoted successfully!");
        }
    }


    public void addVoteToComment() {

    }

    public boolean isUserLoggedIn() {
        return UserContext.isLoggedIn();
    }

    public void userLogout() {
        UserContext.logout();
    }
}
