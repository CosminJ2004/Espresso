package service;

import model.Post;
import model.Comment;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class PostService {

    private static final int MIN_VOTES_FOR_STAR = 10;

    private Map<Integer, Post> posts = new HashMap<>();
    private static Map<Integer, List<Comment>> commentsMap = new HashMap<>();
    private static Map<Integer, Map<String, String>> votesMap = new HashMap<>();
    private static Map<Integer, Integer> votesCountMap = new HashMap<>();

    // Adaugă post
    public void addPost(Post post) {
        posts.put(post.getId(), post);
        commentsMap.put(post.getId(), new ArrayList<>());
        votesMap.put(post.getId(), new HashMap<>());
        votesCountMap.put(post.getId(), 0);
    }

    // Votare Upvote
    public boolean upvote(Post post, String username) {
        return vote(post, username, "upvote");
    }

    // Votare Downvote
    public boolean downvote(Post post, String username) {
        return vote(post, username, "downvote");
    }

    private boolean vote(Post post, String username, String voteType) {
        int postId = post.getId();
        Map<String, String> userVotes = votesMap.get(postId);
        Integer currentVotes = votesCountMap.get(postId);

        String previousVote = userVotes.get(username);
        if (voteType.equals(previousVote)) {
            System.out.println("You already voted this post.");
            return false;
        }

        if ("upvote".equals(voteType)) {
            if ("downvote".equals(previousVote)) {
                currentVotes += 2;
            } else {
                currentVotes += 1;
            }
        } else if ("downvote".equals(voteType)) {
            if ("upvote".equals(previousVote)) {
                currentVotes -= 2;
            } else {
                currentVotes -= 1;
            }
        }

        userVotes.put(username, voteType);
        votesCountMap.put(postId, currentVotes);
        return true;
    }

    public int getVotes(Post post) {
        return votesCountMap.getOrDefault(post.getId(), 0);
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
            comment.display(1);
        }
    }

    // Afișare sumarizată
    public String display(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = post.getDateTime().format(formatter);
        int votes = getVotes(post);

        String result = "[" + post.getId() + "] " + post.getSummary() + " (by " + post.getAuthor() + ") Votes: " + votes + " | Posted on: " + formattedDateTime;
        if (votes >= MIN_VOTES_FOR_STAR) {
            result += " ⭐";
        }
        return result;
    }

    // Afișare detaliată
    public void expand(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = post.getDateTime().format(formatter);
        int votes = getVotes(post);

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
                comment.display(1);
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
        votesMap.remove(postId);
        votesCountMap.remove(postId);
        return true;
    }
}
