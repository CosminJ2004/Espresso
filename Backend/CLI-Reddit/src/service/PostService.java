package service;

import model.Post;
import model.Comment;
import logger.*;
import util.Console;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Scanner;

public class PostService {
    private static final PostService instance = new PostService();
    private static final int MIN_VOTES_FOR_STAR = 10;

    private final VoteService voteService;
    private final Scanner scanner;

    private PostService() {
        this.voteService = VoteService.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public static PostService getInstance() {
        return instance;
    }

    private final Map<Integer, Post> posts = new HashMap<>();
    private static final Map<Integer, List<Comment>> commentsMap = new HashMap<>();


    // Adaugă post
    public void addPost(Post post) {
        posts.put(post.getId(), post);
        commentsMap.put(post.getId(), new ArrayList<>());

    }

    public void addComment(Post post, Comment comment) {
        System.out.println("ADDING: post ID = " + post.getId());
        System.out.println("Map before = " + commentsMap);
        commentsMap.computeIfAbsent(post.getId(), k -> new ArrayList<>()).add(comment);
        System.out.println("Map after = " + commentsMap);
    }

    public List<Comment> getComments(Post post) {
        System.out.println("GETTING: post ID = " + post.getId());
        System.out.println("Map = " + commentsMap);
        return commentsMap.getOrDefault(post.getId(), new ArrayList<>());
    }


    // Afișează toate comentariile
    public void showAllComments(Post post) {
        List<Comment> commentList = getComments(post);
        CommentService commentService = CommentService.getInstance();
        for (Comment comment : commentList) {
            commentService.displayComment(comment, 1);
        }
    }

    // Afișare sumarizată
    public String display(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = post.getDateTime().format(formatter);
        int votes = voteService.getVoteCount(post);

        String result = "[" + post.getId() + "] " + post.getSummary() + " (by " + post.getAuthor().getUsername() + ") Votes: " + votes + " | Posted on: " + formattedDateTime;
        if (votes >= MIN_VOTES_FOR_STAR) {
            result += " ⭐";
        }
        return result;
    }

    // Afișare detaliată
    public void expand(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = post.getDateTime().format(formatter);
        int votes = voteService.getVoteCount(post);


        String result = "[" + post.getId() + "] " + post.getSummary() + " " + post.getContent() + " (by " + post.getAuthor() + ") Votes: " + votes + " | Posted on: " + formattedDateTime;
        if (votes >= MIN_VOTES_FOR_STAR) {
            result += " ⭐";
        }
        System.out.println(result);

        List<Comment> commentList = getComments(post);
        System.out.println("DEBUG: post ID = " + post.getId() + ", comments count = " + commentList.size());

        if (commentList.isEmpty()) {
            System.out.println("There are no comments");
        } else {
            CommentService commentService = CommentService.getInstance();
            for (Comment comment : commentList) {
                commentService.displayComment(comment, 1);
            }
        }
    }
    public Post getPostById(int id) {
        return posts.get(id);
    }

    public List<Post> getAllPosts() {
        return new ArrayList<>(posts.values());
    }

    public boolean deletePostById(int postId) {
        if (!posts.containsKey(postId)) {
            return false;
        }

        posts.remove(postId);
        commentsMap.remove(postId);

        return true;
    }

    public void createPost() {
        Log.log(LogLevel.INFO, "Creating new post for user: " + UserService.getCurrentUser().getUsername());
        System.out.print("Enter summary: ");
        String summary = scanner.nextLine();

        System.out.print("Enter content: ");
        String content = scanner.nextLine();

        Post post = new Post(UserService.getCurrentUser(), summary, content);
        addPost(post);

        System.out.println("Post created with ID: " + post.getId());
    }

    public void showPost() {
        System.out.print("Enter post ID: ");
        int postId = Integer.parseInt(scanner.nextLine());
        Post postTemp = getPostById(postId);
        if (postTemp != null) {
            display(postTemp);
        } else {
            System.out.println("Post not found.");
        }
    }

    public void expandPost() {
        System.out.print("Enter post ID to expand: ");
        int postId = Integer.parseInt(scanner.nextLine());

        Post post = getPostById(postId);
        if (post == null) {
            System.out.println("Post not found.");
            return;
        }

        expand(post);
    }

    public Post openPost() {
        try {
            Log.log(LogLevel.INFO, "User attempting to open post");
            int currentPostID = Console.readInt("Choose the Post ID you wish to open: ");
            Post currentPost = getPostById(currentPostID);
            
            if (currentPost != null) {
                Log.log(LogLevel.INFO, "Post opened successfully: " + currentPostID + " by user: " + UserService.getCurrentUser().getUsername());
                return currentPost;
            } else {
                Log.log(LogLevel.WARN, "Failed to open post with ID: " + currentPostID);
                return null;
            }
        } catch (Exception e) {
            Log.log(LogLevel.ERROR, "Failed to open post: " + e.getMessage());
            return null;
        }
    }

    public void deletePost() {
        System.out.print("Enter post ID to delete: ");
        int postId = Integer.parseInt(scanner.nextLine());

        Post post = getPostById(postId);
        if (post == null) {
            System.out.println("Post not found.");
            return;
        }
        
        Log.log(LogLevel.INFO, "User attempting to delete post ID: " + postId);
        
        if (post.getAuthor().equals(UserService.getCurrentUser())) {
            boolean deleted = deletePostById(postId);
            if (deleted) {
                Log.log(LogLevel.INFO, "Post deleted successfully - ID: " + postId + " by user: " + UserService.getCurrentUser().getUsername());
                System.out.println("Post deleted successfully.");
            } else {
                System.out.println("Failed to delete post.");
            }
        } else {
            Log.log(LogLevel.WARN, "Unauthorized delete attempt on post ID: " + postId + " by user: " + UserService.getCurrentUser().getUsername());
            System.out.println("You can only delete your own posts.");
        }
    }
}
