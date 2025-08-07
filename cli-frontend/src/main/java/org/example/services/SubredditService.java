package org.example.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.example.models.Post;
import org.example.models.Subreddit;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class SubredditService {
    private static SubredditService instance;

    public static SubredditService getInstance() {
        if (instance == null) {
            instance = new SubredditService();
        }
        return instance;
    }

    public HashMap<String, Subreddit> populateSubreddits(HashMap<String, Subreddit> subreddits, JsonArray jsonArray) {
        Gson gson = new Gson();
        Type postArray = new TypeToken<List<Post>>() {}.getType();
        List<Post> posts = gson.fromJson(jsonArray, postArray);
        for (Post post : posts) {
            if (!subreddits.containsKey(post.getSubreddit())) {
                subreddits.put(post.getSubreddit(), new Subreddit(post.getSubreddit(), post.getSubreddit(), "WIP", 1, "WIP", LocalDateTime.now().toString()));
            }
            subreddits.get(post.getSubreddit()).addPost(post);
        }
        return subreddits;
    }
}
