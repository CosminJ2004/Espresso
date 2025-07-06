import java.util.*;
public class Service {

    List<Post> posts = new ArrayList<>();
    List<CommentPost> commentPosts = new ArrayList<>();
    List<CommentCom> commentComs = new ArrayList<>();
    User user = new User();
    private int currentPostID;
    private Post currentPost;

    public boolean login(Scanner scanner) {
        System.out.println("Please enter your username: ");
        String username = scanner.next();
        System.out.println("Please enter your password: ");
        String password = scanner.next();
        user.login(username, password);
        return user.isLoggedIn();
    }

    public boolean register(Scanner scanner) {
        System.out.println("Please enter the desired username: ");
        String username = scanner.next();
        System.out.println("Please enter the desired password: ");
        String password = scanner.next();
        user.register(username, password);
        return user.isLoggedIn() && user.login(username, password);
    }

    public void createPost(Scanner scanner) {
        System.out.print("Enter the summary of the post: ");
        String summary = scanner.nextLine();
        System.out.print("Enter the content of the post: ");
        String content = scanner.nextLine();
        Post post = new Post(user.getUsername(), summary, content);
        posts.add(post);
    }

    public void showPosts(Scanner scanner) {
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

    public void openPost(Scanner scanner) {
        System.out.println("Choose the Post Id you wish to open:");
        currentPostID = scanner.nextInt(); scanner.nextLine();
        currentPost = getPostById(currentPostID);
        expandPost();
    }

    public void expandPost() {
        System.out.println(currentPost.expand());
    }

    public void deletePost(Scanner scanner) {
        posts.remove(currentPost);
    }

    public void addCommentToPost(Scanner scanner) {

//        CommentPost commentPost=new CommentPost(user, )


    }

    public void addCommentToComment(Scanner scanner) {

    }

    public void addVoteToPost(Scanner scanner) {
        currentPost.upvote();
    }


    public void addVoteToComment(Scanner scanner) {
    }

    public boolean isUserLoggedIn() {
        return user.isLoggedIn();
    }

    public void userLogout() {
        user.logout();
    }
}
