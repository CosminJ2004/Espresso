package org.example.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.example.apiservices.ApiPostService;
import org.example.models.Post;
import org.example.models.Subreddit;
import org.example.ui.PostUI;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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

    public void showPosts(Subreddit subreddit) {
        for (Post post : subreddit.getSubPosts()) {
            postUI.renderPost(post);
        }
    }

    public HashMap<String, Subreddit> populateSubreddits(HashMap<String, Subreddit> subreddits, JsonArray jsonArray) {
        Gson gson = new Gson();
        Type postArray = new TypeToken<List<Post>>() {}.getType();
        List<Post> posts = gson.fromJson(jsonArray, postArray);
        for (Post post : posts) {
            if (!subreddits.containsKey(post.getSubreddit())) {
                subreddits.put(post.getSubreddit(), new Subreddit(post.getSubreddit(), post.getSubreddit(), "WIP", 1, 1, "WIP", LocalDateTime.now().toString()));
            }
            subreddits.get(post.getSubreddit()).addPost(post);
        }
        return subreddits;
    }
}
