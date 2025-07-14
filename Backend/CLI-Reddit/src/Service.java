import java.util.*;

import logger.*;
import model.Comment;

import model.Post;
import service.CommentService;
import service.PostService;
import service.UserService;
import util.InputReader;

public class Service {

    List<Post> posts = new ArrayList<>();
    List<Comment> commentsAll = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    UserService userService=new UserService();
    LoggerManager logger = new LoggerManager();
    ILogger fileLogger = new FileLogger(LogLevel.DEBUG, "app.log");
    InputReader inputReader = new InputReader();
    PostService postService=new PostService();
    CommentService commentService=new CommentService();




    private int currentPostID;
    private Post currentPost;

    public Service(){
        logger.addLogger(fileLogger);
    }


    public boolean login() {
        logger.log(LogLevel.INFO, "Login attempt initiated");
        String username = inputReader.readUsername("Please enter your username: ");
        String password = inputReader.readPassword("Please enter your password: ");

        logger.log(LogLevel.DEBUG, "Attempting login for user: " + username);
        userService.login(username, password);

        boolean loginSuccess = UserService.isLoggedIn();
        if (loginSuccess) {
            logger.log(LogLevel.INFO, "User logged in successfully: " + username);
        } else {
            logger.log(LogLevel.WARN, "Login failed for user: " + username);
        }

        return loginSuccess;
    }

    public boolean register() {
        logger.log(LogLevel.INFO, "Registration attempt initiated");
        String username = inputReader.readUsername("Please enter the desired username: ");
        String password = inputReader.readPassword("Please enter the desired password: ");

        logger.log(LogLevel.DEBUG, "Attempting registration for user: " + username);
        userService.register(username, password);

        boolean registrationSuccess = UserService.isLoggedIn() && userService.login(username, password);
        if (registrationSuccess) {
            logger.log(LogLevel.INFO, "User registered and logged in successfully: " + username);
        } else {
            logger.log(LogLevel.WARN, "Registration failed for user: " + username);
        }

        return registrationSuccess;
    }

    public void createPost() {
        logger.log(LogLevel.INFO, "Creating new post for user: " + UserService.getCurrentUser().getUsername());
        System.out.print("Enter summary: ");
        String summary = scanner.nextLine();

        System.out.print("Enter content: ");
        String content = scanner.nextLine();

        Post post = new Post(userService.getCurrentUser(), summary, content);
        postService.addPost(post);

        System.out.println("Post created with ID: " + post.getId());
    }

    public void showPost() {
        logger.log(LogLevel.DEBUG, "Displaying " + posts.size() + " posts");
        System.out.print("Enter post ID: ");
        int postId = Integer.parseInt(scanner.nextLine());
        Post postTemp=postService.getPostById(postId);
        postService.display(postTemp);


    }
    public void showAllPosts() {
        for (Post post : postService.getAllPosts()) {
            System.out.println(postService.display(post));
        }
    }
    public void expandPost() {
        System.out.print("Enter post ID to expand: ");
        int postId = Integer.parseInt(scanner.nextLine());

        Post post = postService.getPostById(postId);
        if (post == null) {
            System.out.println("Post not found.");
            return;
        }

        postService.expand(post);
    }

    public boolean openPost() {
        try {
            logger.log(LogLevel.INFO, "User attempting to open post");
            currentPostID = inputReader.readId("Choose the Post ID you wish to open: ");
            currentPost = postService.getPostById(currentPostID);
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Failed to open post: " + e.getMessage());
        }
        if (currentPost != null) {
            logger.log(LogLevel.INFO, "Post opened successfully: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
            return false;
        } else {
            logger.log(LogLevel.WARN, "Failed to open post with ID: " + currentPostID);
            return true;

        }
    }


    public void deletePost() {
        System.out.print("Enter post ID to delete: ");
        int postId = Integer.parseInt(scanner.nextLine());

        Post post = postService.getPostById(postId);
        if (post == null) {
            System.out.println("Post not found.");
            return;
        }
        logger.log(LogLevel.INFO, "User attempting to delete post ID: " + postId);
        currentPost=postService.getPostById(postId);
        if (currentPost.getAuthor().equals(UserService.getCurrentUser())) {
            postService.deletePost(postId);
            logger.log(LogLevel.INFO, "Post deleted successfully - ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
            System.out.println("Post deleted successfully.");
        } else {
            logger.log(LogLevel.WARN, "Unauthorized delete attempt on post ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
            System.out.println("You can only delete your own posts.");
        }
    }

    public void addCommentToPost() {
        System.out.print("Enter post ID to comment: ");
        int postId = Integer.parseInt(scanner.nextLine());

        Post post = postService.getPostById(postId);
        if (post == null) {
            System.out.println("Post not found.");
            return;
        }
        currentPost = postService.getPostById(postId);
        if (currentPost == null) {
            System.out.println("No post selected.");
            return;
        }
        //chestia asta cu check de post poate fi scoase intro metoda e TODO

        String text = inputReader.readText("Write your comment: ");

        commentService.addComment(UserService.getCurrentUser(), text, currentPost, null); // trebuie să ai o listă de comentarii în `Post`


    }

    public void addCommentToComment() {
        System.out.print("Enter comment ID to comment: ");
        int commentId = Integer.parseInt(scanner.nextLine());

        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            System.out.println("Comment not found.");
            return;
        }
        //chestia asta cu check de post/commnet poate fi scoase intro metoda e TODO

        String text = inputReader.readText("Write your comment: ");
        Comment newComment = new Comment(UserService.getCurrentUser(), text, currentPost, comment);

        commentService.addComment(UserService.getCurrentUser(),text,currentPost,newComment); // trebuie să ai o listă de comentarii în `Post`


        logger.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
                " added comment ID " + comment.getId() + " to post ID " + currentPostID);
    }
//    public void replyToComment() {
//        if (currentPost == null) {
//            System.out.println("No post selected.");
//            return;
//        }
//
//        showCommentsForCurrentPost(); // Afișează comentariile și ID-urile
//
//        int commentId = inputReader.readInt("Enter the ID of the comment to reply to: ");
//        Comment parent = findCommentById(currentPost.getComments(), commentId);
//        if (parent == null) {
//            System.out.println("Comment not found.");
//            return;
//        }
//
//        String text = inputReader.readText("Write your reply: ");
//        Comment reply = new Comment(UserService.getCurrentUser(), text, currentPost, parent);
//
//        parent.addReply(reply);  // legăm comentariul ca reply
//        commentsAll.add(reply);
//
//        logger.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
//                " replied to comment ID " + parent.getId() + " with comment ID " + reply.getId());
//    }

//    public void addCommentToComment() {
//        logger.log(LogLevel.INFO, "User attempting to add comment to comment");
//        int commentId = inputReader.readId("Choose the comment id you wish to comment on: ");
//
//
//        boolean found = false;
//        Comment currentComment = null;
//
//        for (int i = 0; i < commentsAll.size(); i++) {
//            if (commentsAll.get(i).getId() == commentId) {
//                currentComment = commentsAll.get(i);
//                found = true;
//                break;
//            }
//        }
//
//        if (!found || currentComment == null) {
//            logger.log(LogLevel.WARN, "Comment not found with ID: " + commentId);
//            System.out.println("Comment not found.");
//            return;
//        }
//
//        String textComment = inputReader.readText("Write your comment:");
//
//        CommentCom commentCom = new CommentCom(UserService.getCurrentUser(), textComment); // creating the comment
//
//
//        // Cast & Add reply
//        if (currentComment instanceof CommentPost) {
//            CommentPost cp = (CommentPost) currentComment;
//            cp.addReply(commentCom); // add as reply
////            cp.showComment();
//
//        } else if (currentComment instanceof CommentCom) {
//            CommentCom cc = (CommentCom) currentComment;
//            cc.addReply(commentCom); // add as reply
////            cc.showReplies();
//
//        }
//        commentsAll.add(commentCom);
//
//    }
//    public void upVoteToPost() {
//        logger.log(LogLevel.INFO, "User attempting to upvote post ID: " + currentPostID);
//        if (currentPost.upvote(UserService.getCurrentUser().getUsername())) {
//            logger.log(LogLevel.INFO, "Post upvoted successfully - ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
//            System.out.println("Post upvoted successfully!");
//        } else {
//            logger.log(LogLevel.WARN, "Failed to upvote post ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
//        }
//    }
//
//    public void downVoteToPost() {
//        logger.log(LogLevel.INFO, "User attempting to downvote post ID: " + currentPostID);
//        if (currentPost.downvote(UserService.getCurrentUser().getUsername())) {
//            logger.log(LogLevel.INFO, "Post downvoted successfully - ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
//            System.out.println("Post downvoted successfully!");
//        } else {
//            logger.log(LogLevel.WARN, "Failed to downvote post ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
//        }
//    }


    public void upVoteToComment() {
        logger.log(LogLevel.INFO, "User attempting to upvote comment");
        System.out.println("Enter the comment ID to upvote:");
        int commentId = scanner.nextInt();
        scanner.nextLine();

        for (Comment comment : commentsAll) {
            if (comment.getId() == commentId) {
                if (comment.upvote(UserService.getCurrentUser().getUsername())) {
                    logger.log(LogLevel.INFO, "Comment upvoted successfully - ID: " + commentId + " by user: " + UserService.getCurrentUser().getUsername());
                    System.out.println("Comment upvoted successfully!");
                } else {
                    logger.log(LogLevel.WARN, "Failed to upvote comment ID: " + commentId + " by user: " + UserService.getCurrentUser().getUsername());
                }
                return;
            }
        }

        logger.log(LogLevel.WARN, "Comment not found with ID: " + commentId);
        System.out.println("Comment not found.");
    }

    public void downVoteToComment() {
        int commentId = inputReader.readId("Enter the comment id you wish to downvote: ");
        logger.log(LogLevel.INFO, "User attempting to downvote comment");

        for (Comment comment : commentsAll) {
            if (comment.getId() == commentId) {
                if (comment.downvote(UserService.getCurrentUser().getUsername())) {
                    logger.log(LogLevel.INFO, "Comment downvoted successfully - ID: " + commentId + " by user: " + UserService.getCurrentUser().getUsername());
                    System.out.println("Comment downvoted successfully!");
                } else {
                    logger.log(LogLevel.WARN, "Failed to downvote comment ID: " + commentId + " by user: " + UserService.getCurrentUser().getUsername());
                }
                return;
            }
        }

        logger.log(LogLevel.WARN, "Comment not found with ID: " + commentId);
        System.out.println("Comment not found.");
    }


    public boolean isUserLoggedIn() {
        return UserService.isLoggedIn();
    }

    public void userLogout() {
        logger.log(LogLevel.INFO, "User logout: " + UserService.getCurrentUser().getUsername());
        UserService.logout();
    }
}
