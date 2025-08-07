package org.example.services;

import com.google.gson.Gson;
import org.example.apiservices.ApiPostService;
import org.example.models.Post;
import org.example.models.Subreddit;
import org.example.ui.PostUI;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;

public class PostService {
    private static PostService instance;
    private ApiPostService apiPostService;
    private PostUI postUI = PostUI.getInstance();

    public static PostService getInstance() {
        if (instance == null) {
            instance = new PostService();
        }
        return instance;
    }

    //POST
    public void createPost(HashMap<String, Subreddit> subreddits, Gson gson) throws Exception {
        ArrayList<String> postDetails = postUI.getPostDetailsFromUser();
        String title = postDetails.get(0);
        String content = postDetails.get(1);
        String author = postDetails.get(2);
        String subreddit = postDetails.get(3);
        String json = String.format("""
        {
            "title": "%s",
            "content": "%s",
            "author": "%s",
            "subreddit": "%s"
            
        }
        """, title, content, author, subreddit);

        Post post = gson.fromJson(apiPostService.handlePost(json), Post.class);
        subreddits.get(post.getSubreddit()).addPost(post);
    }
    //PUT
    public void editPost(HashMap<String, Subreddit> subreddits, Gson gson) throws Exception {
        ArrayList<String> updatedDetails = postUI.getUpdatedPostDetailsFromUser();
        long id = Long.parseLong(updatedDetails.get(0));
        String title = updatedDetails.get(1);
        String content = updatedDetails.get(2);
        String json = String.format("""
        {
            "title": "%s",
            "content": "%s",
        }
        """, title, content);
        Post post = gson.fromJson(apiPostService.handlePut(json, id), Post.class);
        subreddits.get(post.getSubreddit()).addPost(post);
    }
    //DELETE
    public void deletePost(HashMap<String, Subreddit> subreddits) throws Exception {
        long id = postUI.getPostIDFromUser();
        apiPostService.handleDelete(id);
        for(Subreddit subreddit : subreddits.values()) {
            if(subreddit.getSubPosts().containsKey(id)) {
                subreddit.getSubPosts().remove(id);
                break;
            }
        }
    }
}
