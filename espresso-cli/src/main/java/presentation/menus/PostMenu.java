package presentation.menus;

import objects.domain.Post;
import presentation.handlers.PostHandler;
import presentation.handlers.CommentHandler;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import presentation.AppState;
import infra.http.ApiResult;

import java.util.List;

public class PostMenu {
    private final PostHandler postHandler;
    private final CommentHandler commentHandler;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public PostMenu(PostHandler postHandler, CommentHandler commentHandler, ConsoleIO io, Renderer ui) {
        this.postHandler = postHandler;
        this.commentHandler = commentHandler;
        this.io = io;
        this.ui = ui;
    }

    public void run() {
        boolean stay = true;
        while (stay && appState.isRunning() && appState.isLoggedIn()) {
            ui.displayPostMenu();
            String option = io.readLine("> ").trim();
            switch (option.trim()) {
                case "1":
                    handleCreatePost();
                    break;
                case "2":
                    handleViewPosts();
                    break;
                case "3":
                    return; // Back to Main Menu
                default:
                    ui.displayInfo("Please enter a valid option (1-3).");
            }
        }
    }

    private void handleCreatePost() {
        postHandler.handleCreatePost();
    }

    private void handleViewPosts() {
        ApiResult<List<Post>> result = postHandler.getAllPosts();

        if (result == null) {
            ui.displayError("Unexpected null response from service.");
            return;
        }
        if (!result.isSuccess()) {
            ui.displayError(result.getError() != null ? result.getError() : "Failed to load posts.");
            return;
        }

        List<Post> posts = result.getData();
        if (posts == null || posts.isEmpty()) {
            ui.displayInfo("No posts yet. Be the first to create one!");
            return;
        }

        ui.displayPosts(posts);
        handlePostSelection(posts);
    }

    private void handlePostSelection(List<Post> posts) {
        while (true) {
            ui.displayPostSelectionMenu();
            String option = io.readLine("> ").trim();

            switch (option.trim()) {
                case "1":
                    selectPostForAction(posts);
                    break;
                case "2":
                    return; // Back to Posts Menu
                default:
                    ui.displayInfo("Please enter a valid option (1-2).");
            }
        }
    }

    private void handlePostActionMenu(Post selectedPost) {
        while (true) {
            ui.displayPostActionMenu(selectedPost);
            String option = io.readLine("> ").trim();

            switch (option.trim()) {
                case "1":
                    postHandler.handleEditPost(selectedPost);
                    return;
                case "2":
                    postHandler.handleDeletePost(selectedPost);
                    return;
                case "3":
                    postHandler.handleVotePost(selectedPost);
                    return;
                case "4":
                    handleCommentSection(selectedPost);
                    return;
                case "5":
                    return; // Back to post selection
                default:
                    ui.displayInfo("Please enter a valid option (1-5).");
            }
        }
    }

    private void selectPostForAction(List<Post> posts) {
        String postNumberInput = io.readLine("Enter post number (1-" + posts.size() + "): ");
        if (postNumberInput == null || postNumberInput.trim().isEmpty()) {
            ui.displayError("Post number cannot be empty.");
            return;
        }

        try {
            int postNumber = Integer.parseInt(postNumberInput.trim());
            if (postNumber < 1 || postNumber > posts.size()) {
                ui.displayError("Invalid post number. Please enter a number between 1 and " + posts.size() + ".");
                return;
            }

            Post selectedPost = posts.get(postNumber - 1);
            ui.displayPost(selectedPost);
            handlePostActionMenu(selectedPost);

        } catch (NumberFormatException e) {
            ui.displayError("Invalid number format. Please enter a valid number.");
        }
    }

    private void handleCommentSection(Post selectedPost) {
        new CommentMenu(commentHandler, io, ui, selectedPost).run();
    }
}
