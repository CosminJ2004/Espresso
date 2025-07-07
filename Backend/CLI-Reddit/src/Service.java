import java.util.*;
public class Service {

    List<Post> posts = new ArrayList<>();
    List<CommentPost> commentPosts = new ArrayList<>();
    List<CommentCom> commentComs = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    User user = new User();
    private int currentPostID;
    private Post currentPost;

    public boolean login() {
        System.out.println("Please enter your username: ");
        String username = scanner.next();
        System.out.println("Please enter your password: ");
        String password = scanner.next();
        user.login(username, password);
        return user.isLoggedIn();
    }

    public boolean register() {
        System.out.println("Please enter the desired username: ");
        String username = scanner.next();
        System.out.println("Please enter the desired password: ");
        String password = scanner.next();
        user.register(username, password);
        return user.isLoggedIn() && user.login(username, password);
    }

    public void createPost() {
        System.out.println("Enter the summary of the post:");
        String summary = scanner.nextLine(); scanner.nextLine();
        System.out.println("Enter the content of the post:");
        String content = scanner.nextLine();
        Post post = new Post(user.getUsername(), summary, content);
        posts.add(post);
    }

    public void showPosts() {
        for (Post post : posts) {
            String msg = post.display();
            System.out.println(msg);
        }
    }

    public Post getPostById(int postID) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == postID) {
                return posts.get(i);
            }
        }

        System.out.println("Post not found.");
        return null;
    }

    public void openPost() {
        System.out.println("Choose the Post Id you wish to open:");
        currentPostID = scanner.nextInt(); scanner.nextLine();
        currentPost = getPostById(currentPostID);
    }

    public void expandPost() {
        currentPost.expand();
    }

    public void deletePost() {
        posts.remove(currentPost);
    }

    public void addCommentToPost() {
        System.out.println("Write your comment:");
        String textComment = scanner.nextLine();
        //casting and adding them to the list of comments of posts
        CommentPost commentPost = new CommentPost(user, textComment, currentPost);
        commentPosts.add(commentPost);//adding also in a list
        currentPost.addComment(commentPost);//adding comments to a post object
    }

    public void addCommentToComment() {

    }

    public void addVoteToPost() {
        currentPost.upvote();
    }


    public void addVoteToComment() {
    }

    public boolean isUserLoggedIn() {
        return user.isLoggedIn();
    }

    public void userLogout() {
        user.logout();
    }
}
