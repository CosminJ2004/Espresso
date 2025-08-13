package presentation.views;

import infra.http.ApiResult;
import objects.domain.Post;
import objects.dto.PostRequestDto;
import presentation.AppState;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.PostService;

import java.util.List;

public class PostMenuManager {
    private static final String DEFAULT_SUBREDDIT = "echipa3_general";
    private final PostService postService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public PostMenuManager(PostService postService, ConsoleIO io, Renderer ui) {
        this.postService = postService;
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
                    // Create Post wihtout image
                    handleCreatePost();
                    break;
                case "2":
                    // Create Post with image
                    break;
                case "3":
                    // View Posts
                    handleViewPosts();
                    break;
                default:
                    ui.displayInfo("Please enter a valid option.");
            }
        }
    }

    private void handleViewPosts() {
        ApiResult<List<Post>> result = postService.getAll();

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
    }

    private void handleCreatePost() {
        if (!appState.isLoggedIn()) {
            ui.displayError("You must be logged in to create a post.");
            return;
        }
        String currentUsername = appState.getCurrentUser().username();
        String title = io.readLine("Enter post title: ");
        String content = io.readLine("Enter post content: ");

        PostRequestDto requestDto = new PostRequestDto(title, content, currentUsername, DEFAULT_SUBREDDIT);

        ApiResult<Post> result = postService.create(requestDto);
        if (result.isSuccess()) {
            ui.displaySuccess("Post created successfully!");
            ui.displayPost(result.getData());
        } else {
            ui.displayError("Failed to create post: " + result.getError());
        }
    }

}
