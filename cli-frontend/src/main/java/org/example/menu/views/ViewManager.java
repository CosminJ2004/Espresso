package org.example.menu.views;

import com.google.gson.Gson;
import org.example.apiservices.*;
import org.example.models.Subreddit;
import org.example.services.*;
import org.example.ui.PostUI;

import java.net.http.HttpClient;
import java.util.HashMap;

public class ViewManager {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private static ViewManager instance;
    private HashMap<String, Subreddit> subreddits;
    private HashMap<ViewID, View> views;
    private ViewID currentViewID;

    private final PostUI postUI = PostUI.getInstance();

    private final PostService postService = PostService.getInstance(gson);
    private final CommentService commentService = CommentService.getInstance();
    private final UserService userService = UserService.getInstance();
    private final VoteService voteService = VoteService.getInstance();
    private final SubredditService subredditService = SubredditService.getInstance();

    private final ApiPostService apiPostService = ApiPostService.getInstance(client);
    private final ApiCommentService apiCommentService = ApiCommentService.getInstance(client);
    private final ApiUserService apiUserService = ApiUserService.getInstance(client);
    private final ApiVoteService apiVoteService = ApiVoteService.getInstance(client);

    private ViewManager() {
        this.views = new HashMap<>();
        this.subreddits = new HashMap<>();
        currentViewID = ViewID.MAIN_MENU;
    }

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
            instance.initAllViews();
        }
        return instance;
    }

    private void initAllViews() {
        views.put(ViewID.MAIN_MENU, ViewSetup.initMainMenu());
        views.put(ViewID.POST_MENU, ViewSetup.initPostMenu());
        //views.put(ViewID.COMMENT_MENU, ViewSetup.initCommentMenu());
        //views.put(ViewID.VOTE_MENU, ViewSetup.initVoteMenu());
        //views.put(ViewID.USER_MENU, ViewSetup.initUserMenu());

        ViewSetup.linkViews(views);
    }

    public PostService getPostService() {
        return postService;
    }

    public CommentService getCommentService() {
        return commentService;
    }

    public UserService getUserService() {
        return userService;
    }

    public VoteService getVoteService() {
        return voteService;
    }

    public SubredditService getSubredditService() {
        return subredditService;
    }

    public ApiPostService getApiPostService() {
        return apiPostService;
    }

    public ApiCommentService getApiCommentService() {
        return apiCommentService;
    }

    public ApiUserService getApiUserService() {
        return apiUserService;
    }

    public ApiVoteService getApiVoteService() {
        return apiVoteService;
    }

    public HashMap<String, Subreddit> getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(HashMap<String, Subreddit> subreddits) {
        this.subreddits = subreddits;
    }

    public void switchToNextView(ViewID viewID) {
        currentViewID = viewID;
    }

    public View getCurrentViewObject() {
        return views.get(currentViewID);
    }

    public ViewID getCurrentViewID() {
        return currentViewID;
    }

    public PostUI getPostUI() {
        return postUI;
    }
}
