import java.util.*;

import logger.*;
import model.Comment;
import model.CommentCom;
import model.CommentPost;
import model.Post;
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
        String summary = inputReader.readText("Enter the summary of the post: ");
        String content= inputReader.readText("Enter the content of the post: ");

        try {
            Post post = new Post(UserService.getCurrentUser().getUsername(), summary, content);
            posts.add(post);
            logger.log(LogLevel.INFO, "Post created successfully by user: " + UserService.getCurrentUser().getUsername() + ", Post ID: " + post.getId());
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Failed to create post for user: " + UserService.getCurrentUser().getUsername() + " - " + e.getMessage());
        }

    }

    public void showPosts() {
        logger.log(LogLevel.DEBUG, "Displaying " + posts.size() + " posts");
        if(posts.isEmpty()){
            System.out.println("There are no posts");
            return;
        }
        for (Post post : posts) {
            String msg = post.display();
            System.out.println(msg);
        }
    }

    public Post getPostById(int postID) {
        logger.log(LogLevel.DEBUG, "Searching for post with ID: " + postID);
        for (Post post : posts) {
            if (post.getId() == postID) {
                logger.log(LogLevel.DEBUG, "Post found with ID: " + postID);
                return post;
            }
        }
        logger.log(LogLevel.WARN, "Post not found with ID: " + postID);
        System.out.println("Post not found.");
        return null;
    }

    public boolean openPost() {
        try {
            logger.log(LogLevel.INFO, "User attempting to open post");
            currentPostID = inputReader.readId("Choose the Post ID you wish to open: ");
            currentPost = getPostById(currentPostID);
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

    public void expandPost() {
        logger.log(LogLevel.INFO, "Expanding post ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
        if (currentPost != null) {
            currentPost.expand();
            logger.log(LogLevel.INFO, "Post expanded successfully: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
        } else {
            logger.log(LogLevel.WARN, "Failed to expand post with ID: " + currentPostID);
        }
    }

    public void deletePost() {
        logger.log(LogLevel.INFO, "User attempting to delete post ID: " + currentPostID);
        if (currentPost.getAuthor().equals(UserService.getCurrentUser().getUsername())) {
            posts.remove(currentPost);
            logger.log(LogLevel.INFO, "Post deleted successfully - ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
            System.out.println("Post deleted successfully.");
        } else {
            logger.log(LogLevel.WARN, "Unauthorized delete attempt on post ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
            System.out.println("You can only delete your own posts.");
        }
    }

    public void addCommentToPost() {
        String textComment = inputReader.readText("Write your comment: ");
        //casting and adding them to the list of comments of posts
        CommentPost commentPost = new CommentPost(UserService.getCurrentUser(), textComment, currentPost);
//        commentPosts.add(commentPost);//adding also in a list
//        currentPost.addComment(commentPost);//adding comments to a post object
//        commentsAll.add(commentPost); //adding the comment to the list pf all comments
//        logger.log(LogLevel.INFO, "User adding comment to post ID: " + currentPostID);

        try {
            currentPost.addComment(commentPost);
            commentsAll.add(commentPost);
            logger.log(LogLevel.INFO, "Comment added successfully to post ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
        } catch (Exception e) {
            logger.log(LogLevel.ERROR, "Failed to add comment to post ID: " + currentPostID + " - " + e.getMessage());
        }
    }

    public void addCommentToComment() {
        logger.log(LogLevel.INFO, "User attempting to add comment to comment");
        int commentId = inputReader.readId("Choose the comment id you wish to comment on: ");


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
            logger.log(LogLevel.WARN, "Comment not found with ID: " + commentId);
            System.out.println("Comment not found.");
            return;
        }

        String textComment = inputReader.readText("Write your comment:");

        CommentCom commentCom = new CommentCom(UserService.getCurrentUser(), textComment); // creating the comment


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
        logger.log(LogLevel.INFO, "User attempting to upvote post ID: " + currentPostID);
        if (currentPost.upvote(UserService.getCurrentUser().getUsername())) {
            logger.log(LogLevel.INFO, "Post upvoted successfully - ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
            System.out.println("Post upvoted successfully!");
        } else {
            logger.log(LogLevel.WARN, "Failed to upvote post ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
        }
    }

    public void downVoteToPost() {
        logger.log(LogLevel.INFO, "User attempting to downvote post ID: " + currentPostID);
        if (currentPost.downvote(UserService.getCurrentUser().getUsername())) {
            logger.log(LogLevel.INFO, "Post downvoted successfully - ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
            System.out.println("Post downvoted successfully!");
        } else {
            logger.log(LogLevel.WARN, "Failed to downvote post ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
        }
    }


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
