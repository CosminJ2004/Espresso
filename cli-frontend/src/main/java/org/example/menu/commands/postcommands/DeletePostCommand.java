package org.example.menu.commands.postcommands;

import org.example.menu.commands.IMenuCommand;
import org.example.menu.views.View;
import org.example.menu.views.ViewManager;
import org.example.models.Subreddit;

import java.util.HashMap;

public class DeletePostCommand implements IMenuCommand {

    public boolean execute(View view) {
        ViewManager viewManager = view.getViewManager();
        HashMap<String, Subreddit> subreddits = viewManager.getSubreddits();
        try {
            subreddits = viewManager.getSubredditService().populateSubreddits(subreddits, viewManager.getApiPostService().handleGet());
        } catch (Exception e) {
            System.out.println("Error fetching feed");
        }

        try {
            viewManager.getPostService().deletePost(subreddits, viewManager.getApiPostService());
        } catch (Exception e) {
            System.err.println("Error deleting post");
        }

        try {
            subreddits = viewManager.getSubredditService().populateSubreddits(subreddits, viewManager.getApiPostService().handleGet());
        } catch (Exception e) {
            System.out.println("Error fetching feed");
        }
        viewManager.getPostUI().showFeed(subreddits.get("echipa3_general"));

        return true;
    }
}
