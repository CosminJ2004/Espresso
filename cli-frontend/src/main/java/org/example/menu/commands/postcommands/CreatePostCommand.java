package org.example.menu.commands.postcommands;

import org.example.menu.commands.IMenuCommand;
import org.example.menu.views.View;
import org.example.menu.views.ViewManager;
import org.example.models.Post;

public class CreatePostCommand implements IMenuCommand {

    public boolean execute(View view) {
        ViewManager viewManager = view.getViewManager();
        Post post = new Post();
        try {
            post = viewManager.getPostService().createPost(viewManager.getApiPostService());
        } catch (Exception e) {
            System.err.println("Couldn't create post");
        }
        viewManager.getSubreddits().get(post.getSubreddit()).addPost(post);
        viewManager.getPostUI().showFeed(viewManager.getSubreddits().get("echipa3_general")); // Post.getSubreddit() pe viitor
        return true;
    }
}
