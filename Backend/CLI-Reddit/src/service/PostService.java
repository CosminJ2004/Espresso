package service;

import model.Post;
import model.Comment;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class PostService {
    private static final PostService instance = new PostService();
    private static final int MIN_VOTES_FOR_STAR = 10;

    private VoteService voteService;

    private PostService() {
        this.voteService = VoteService.getInstance();
    }

    public static PostService getInstance() {
        return instance;
    }

    private Map<Integer, Post> posts = new HashMap<>();
    private static Map<Integer, List<Comment>> commentsMap = new HashMap<>();


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
        for (Comment comment : commentList) {
            comment.display(1,voteService);
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
            for (Comment comment : commentList) {
                comment.display(1,voteService);
            }
        }
    }
    public Post getPostById(int id) {
        return posts.get(id);
    }

    public List<Post> getAllPosts() {
        return new ArrayList<>(posts.values());
    }
    public boolean deletePost(int postId) {
        if (!posts.containsKey(postId)) {
            return false;
        }

        posts.remove(postId);
        commentsMap.remove(postId);

        return true;
    }
}
