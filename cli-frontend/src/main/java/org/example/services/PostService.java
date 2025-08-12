package org.example.services;

import com.google.gson.Gson;
import org.example.apiservices.ApiPostService;
import org.example.models.Post;
import org.example.models.Subreddit;
import org.example.ui.PostUI;

import java.util.ArrayList;
import java.util.HashMap;

public class PostService {
    private static PostService instance;
    private PostUI postUI = PostUI.getInstance();
    private Gson gson;

    private PostService(Gson gson) {
        this.gson = gson;
    }

    public static PostService getInstance(Gson gson) {
        if (instance == null) {
            instance = new PostService(gson);
        }
        return instance;
    }

    //POST
    public Post createPost(ApiPostService apiPostService) throws Exception {
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

        return gson.fromJson(apiPostService.handlePost(json), Post.class);
    }
    //PUT
    public void editPost(HashMap<String, Subreddit> subreddits, ApiPostService apiPostService) throws Exception {
        ArrayList<String> updatedDetails = postUI.getUpdatedPostDetailsFromUser();
        long id = Long.parseLong(updatedDetails.get(0));
        String title = updatedDetails.get(1);
        String content = updatedDetails.get(2);
        String json = String.format("""
        {
            "author": "current_user",
            "subreddit": "echipa3_general",
            "title": "%s",
            "content": "%s"
        }
        """, title, content);
        Post post = gson.fromJson(apiPostService.handlePut(json, id), Post.class);
        subreddits.get(post.getSubreddit()).addPost(post);
    }
    //DELETE
    public void deletePost(HashMap<String, Subreddit> subreddits, ApiPostService apiPostService) throws Exception {
        long id = postUI.getPostIDFromUser();
        apiPostService.handleDelete(id);
        for(Subreddit subreddit : subreddits.values()) {
            if(subreddit.getSubPosts().containsKey(Long.toString(id))) {
                subreddit.getSubPosts().remove(Long.toString(id));
                break;
            }
        }
    }
}
