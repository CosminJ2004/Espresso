package org.example.menu.commands.postcommands;

import org.example.menu.commands.IMenuCommand;
import org.example.menu.views.View;
import org.example.menu.views.ViewManager;
import org.example.models.Subreddit;

import java.util.HashMap;

public class EditPostCommand implements IMenuCommand {

    public boolean execute(View view) {
        ViewManager viewManager = ViewManager.getInstance();

        HashMap<String, Subreddit> subreddits = viewManager.getSubreddits();
        try {
            subreddits = viewManager.getSubredditService().populateSubreddits(subreddits, viewManager.getApiPostService().handleGet());
        } catch (Exception e) {
            System.out.println("Error fetching feed");
        }

        try {
            viewManager.getPostService().editPost(subreddits, viewManager.getApiPostService());
        } catch (Exception e) {
            System.out.println("Error editing post");
        }
        viewManager.getPostUI().showFeed(subreddits.get("echipa3_general"));



        return true;
    }
}
