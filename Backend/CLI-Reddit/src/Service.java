    import java.util.*;

    import logger.*;
    import model.Comment;

    import model.Post;
    import model.VoteType;
    import service.CommentService;
    import service.PostService;
    import service.UserService;
    import service.VoteService;
    import util.Console;

    public class Service {

        List<Post> posts = new ArrayList<>();
        List<Comment> commentsAll = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        private final UserService userService;
        private final PostService postService;
        private final CommentService commentService;
        private final VoteService voteService;

        private int currentPostID;
        private Post currentPost;

        public Service(UserService userService,PostService postService, CommentService commentService,VoteService voteService){
            this.userService=userService;
            this.commentService=commentService;
            this.postService=postService;
            this.voteService=voteService;

        }


        public boolean login() {
            Log.log(LogLevel.INFO, "Login attempt initiated");
            String username = Console.readCredential("Please enter your username: ");
            String password = Console.readCredential("Please enter your password: ");

            Log.log(LogLevel.DEBUG, "Attempting login for user: " + username);
            userService.login(username, password);

            boolean loginSuccess = UserService.isLoggedIn();
            if (loginSuccess) {
                Log.log(LogLevel.INFO, "User logged in successfully: " + username);
            } else {
                Log.log(LogLevel.WARN, "Login failed for user: " + username);
            }

            return loginSuccess;
        }

        public boolean register() {
            Log.log(LogLevel.INFO, "Registration attempt initiated");
            String username = Console.readCredential("Please enter the desired username: ");
            String password = Console.readCredential("Please enter the desired password: ");

            Log.log(LogLevel.DEBUG, "Attempting registration for user: " + username);
            userService.register(username, password);

            boolean registrationSuccess = UserService.isLoggedIn() && userService.login(username, password);
            if (registrationSuccess) {
                Log.log(LogLevel.INFO, "User registered and logged in successfully: " + username);
            } else {
                Log.log(LogLevel.WARN, "Registration failed for user: " + username);
            }

            return registrationSuccess;
        }

        public void createPost() {
            Log.log(LogLevel.INFO, "Creating new post for user: " + UserService.getCurrentUser().getUsername());
            System.out.print("Enter summary: ");
            String summary = scanner.nextLine();

            System.out.print("Enter content: ");
            String content = scanner.nextLine();

            Post post = new Post(userService.getCurrentUser(), summary, content);
            postService.addPost(post);

            System.out.println("Post created with ID: " + post.getId());
        }

        public void showPost() {
            Log.log(LogLevel.DEBUG, "Displaying " + posts.size() + " posts");
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
                Log.log(LogLevel.INFO, "User attempting to open post");
                currentPostID = Console.readInt("Choose the Post ID you wish to open: ");
                currentPost = postService.getPostById(currentPostID);
            } catch (Exception e) {
                Log.log(LogLevel.ERROR, "Failed to open post: " + e.getMessage());
            }
            if (currentPost != null) {
                Log.log(LogLevel.INFO, "Post opened successfully: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
                return false;
            } else {
                Log.log(LogLevel.WARN, "Failed to open post with ID: " + currentPostID);
                return true;

            }
        }

        public boolean upvoteToPost() {
            System.out.print("Enter post ID to upvote: ");
            int postId = Integer.parseInt(scanner.nextLine());
            Post post =postService.getPostById(postId);
            return voteService.vote(post, userService.getCurrentUser(), VoteType.UPVOTE);
        }

        public boolean downvoteToPost() {
            System.out.print("Enter post ID to downvote: ");
            int postId = Integer.parseInt(scanner.nextLine());
            Post post =postService.getPostById(postId);
            return voteService.vote(post, userService.getCurrentUser(), VoteType.DOWNVOTE);
        }


        public boolean upVoteToComment() {
            System.out.print("Enter comment ID to upvote: ");
            int commentId = Integer.parseInt(scanner.nextLine());
            Comment comment =commentService.getById(commentId);
            return voteService.vote(comment, userService.getCurrentUser(), VoteType.UPVOTE);
        }

        public boolean downVoteToComment() {
            System.out.print("Enter comment ID to downvote: ");
            int commentId = Integer.parseInt(scanner.nextLine());
            Comment comment =commentService.getById(commentId);
            return voteService.vote(comment, userService.getCurrentUser(), VoteType.DOWNVOTE);
        }








        public void deletePost() {
            System.out.print("Enter post ID to delete: ");
            int postId = Integer.parseInt(scanner.nextLine());

            Post post = postService.getPostById(postId);
            if (post == null) {
                System.out.println("Post not found.");
                return;
            }
            Log.log(LogLevel.INFO, "User attempting to delete post ID: " + postId);
            currentPost=postService.getPostById(postId);
            if (currentPost.getAuthor().equals(UserService.getCurrentUser())) {
                postService.deletePost(postId);
                Log.log(LogLevel.INFO, "Post deleted successfully - ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
                System.out.println("Post deleted successfully.");
            } else {
                Log.log(LogLevel.WARN, "Unauthorized delete attempt on post ID: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
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
            currentPost=postService.getPostById(postId);
            if (currentPost == null) {
            System.out.println("No post selected.");
            return;
        }
            //chestia asta cu check de post poate fi scoase intro metoda e TODO

        String text = Console.readText("Write your comment: ");
        Comment comment = commentService.addComment(UserService.getCurrentUser(), text, currentPost, null);

        Log.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
            " added comment ID " + comment.getId() + " to post ID " + postId);
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

            String text = Console.readText("Write your comment: ");
    //        Comment newComment = new Comment(UserService.getCurrentUser(), text, currentPost, comment);

            commentService.addComment(UserService.getCurrentUser(),text,currentPost,comment); // trebuie să ai o listă de comentarii în `Post`


            Log.log(LogLevel.INFO, "User " + UserService.getCurrentUser().getUsername() +
                    " added comment ID " + comment.getId() + " to post ID " + currentPostID);
        }

//        public void upVoteToComment() {
//            Log.log(LogLevel.INFO, "User attempting to upvote comment");
//            System.out.println("Enter the comment ID to upvote:");
//            int commentId = scanner.nextInt();
//            scanner.nextLine();
//
//            for (Comment comment : commentsAll) {
//                if (comment.getId() == commentId) {
//                    if (comment.upvote(UserService.getCurrentUser().getUsername())) {
//                        Log.log(LogLevel.INFO, "Comment upvoted successfully - ID: " + commentId + " by user: " + UserService.getCurrentUser().getUsername());
//                        System.out.println("Comment upvoted successfully!");
//                    } else {
//                        Log.log(LogLevel.WARN, "Failed to upvote comment ID: " + commentId + " by user: " + UserService.getCurrentUser().getUsername());
//                    }
//                    return;
//                }
//            }
//
//            Log.log(LogLevel.WARN, "Comment not found with ID: " + commentId);
//            System.out.println("Comment not found.");
//        }
//
//        public void downVoteToComment() {
//            int commentId = Console.readInt("Enter the comment id you wish to downvote: ");
//            Log.log(LogLevel.INFO, "User attempting to downvote comment");
//
//            for (Comment comment : commentsAll) {
//                if (comment.getId() == commentId) {
//                    if (comment.downvote(UserService.getCurrentUser().getUsername())) {
//                        Log.log(LogLevel.INFO, "Comment downvoted successfully - ID: " + commentId + " by user: " + UserService.getCurrentUser().getUsername());
//                        System.out.println("Comment downvoted successfully!");
//                    } else {
//                        Log.log(LogLevel.WARN, "Failed to downvote comment ID: " + commentId + " by user: " + UserService.getCurrentUser().getUsername());
//                    }
//                    return;
//                }
//            }
//
//            Log.log(LogLevel.WARN, "Comment not found with ID: " + commentId);
//            System.out.println("Comment not found.");
//        }


        public boolean isUserLoggedIn() {
            return UserService.isLoggedIn();
        }

        public void userLogout() {
            Log.log(LogLevel.INFO, "User logout: " + UserService.getCurrentUser().getUsername());
            UserService.logout();
        }
    }
