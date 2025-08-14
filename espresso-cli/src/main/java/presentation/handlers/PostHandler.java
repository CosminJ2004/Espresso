package presentation.handlers;

import infra.http.ApiResult;
import objects.domain.Post;
import objects.domain.Vote;
import objects.domain.VoteType;
import objects.dto.PostRequestDto;
import objects.dto.PostRequestWithImageDto;
import objects.dto.VoteRequestDto;
import presentation.AppState;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.PostService;

import java.util.List;

public class PostHandler {
    private static final String DEFAULT_SUBREDDIT = "echipa3_general";
    private final PostService postService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public PostHandler(PostService postService, ConsoleIO io, Renderer ui) {
        this.postService = postService;
        this.io = io;
        this.ui = ui;
    }

    public void handleCreatePost() {
        if (!appState.isLoggedIn()) {
            ui.displayError("You must be logged in to create a post.");
            return;
        }
        String currentUsername = appState.getCurrentUser().username();
        String title = io.readLine("Enter post title: ");
        String content = io.readLine("Enter post content: ");

        //imagine optional
        String uploadImageChoice = io.readLine("Do you want to upload an image? (yes/no): ");
        
        if ("yes".equalsIgnoreCase(uploadImageChoice.trim())) {
            handleCreatePostWithImage(title, content, currentUsername);
        } else {
            handleCreatePostWithoutImage(title, content, currentUsername);
        }
    }

    public void handleCreatePostWithoutImage(String title, String content, String currentUsername) {
        PostRequestDto requestDto = new PostRequestDto(title, content, currentUsername, DEFAULT_SUBREDDIT);

        ApiResult<Post> result = postService.create(requestDto);
        if (result.isSuccess()) {
            ui.displaySuccess("Post created successfully!");
            ui.displayPost(result.getData());
        } else {
            ui.displayError("Failed to create post: " + result.getError());
        }
    }

    public void handleCreatePostWithImage(String title, String content, String currentUsername) {
        String imagePath = io.readLine("Enter the full path to your image file: ");
        
        if (imagePath == null || imagePath.trim().isEmpty()) {
            ui.displayError("Image path cannot be empty.");
            return;
        }
        //filtru optional
        String filterInput = io.readLine("Enter filter ID (or press Enter to skip): ");
        Long filterId = 1L; // do nothing filter is at one
        if(filterInput.isEmpty() || filterInput == null) {
            ui.displayInfo("No filter ID provided, using default no filter."); // ID 1 : none
        } else {
            try {
                filterId = Long.parseLong(filterInput.trim());
            } catch (NumberFormatException e) {
                ui.displayError("Invalid filter ID. Creating post without filter.");
                filterId = 1L;
            }
        }

        PostRequestWithImageDto requestDto = new PostRequestWithImageDto(
            title, content, currentUsername, DEFAULT_SUBREDDIT, 
            java.nio.file.Path.of(imagePath), filterId
        );

        ApiResult<Post> result = postService.createWithImage(requestDto);
        if (result.isSuccess()) {
            ui.displaySuccess("Post with image created successfully!");
            ui.displayPost(result.getData());
        } else {
            ui.displayError("Failed to create post with image: " + result.getError());
        }
    }

    public void handleEditPost(Post existingPost) {
        String currentUsername = appState.getCurrentUser().username();

        if (!existingPost.author().equals(currentUsername)) {
            ui.displayError("You can only edit your own posts.");
            return;
        }

        String newTitle = io.readLine("Enter new title (or press Enter to keep current): ");
        String newContent = io.readLine("Enter new content (or press Enter to keep current): ");

        if (newTitle.trim().isEmpty()) newTitle = existingPost.title();
        if (newContent.trim().isEmpty()) newContent = existingPost.content();

        PostRequestDto updateDto = new PostRequestDto(newTitle, newContent, currentUsername, existingPost.subreddit());

        ApiResult<Post> result = postService.update(existingPost.id(), updateDto);
        if (result.isSuccess()) {
            ui.displaySuccess("Post updated successfully!");
            ui.displayPost(result.getData());
        } else {
            ui.displayError("Failed to update post: " + result.getError());
        }
    }

    public void handleDeletePost(Post existingPost) {
        String currentUsername = appState.getCurrentUser().username();

        if (!existingPost.author().equals(currentUsername)) {
            ui.displayError("You can only delete your own posts.");
            return;
        }

        String confirmation = io.readLine("Are you sure you want to delete this post? (yes/no): ");
        if ("yes".equalsIgnoreCase(confirmation.trim())) {
            ApiResult<Void> result = postService.delete(existingPost.id());
            if (result.isSuccess()) {
                String successMessage = result.getMessage() != null ? result.getMessage() : "Post deleted successfully!";
                ui.displaySuccess(successMessage);

                ui.displayInfo("Refreshing posts list...");
                ApiResult<List<Post>> updatedPostsResult = postService.getAll();
                if (updatedPostsResult.isSuccess()) {
                    List<Post> updatedPosts = updatedPostsResult.getData();
                    if (updatedPosts != null && !updatedPosts.isEmpty()) {
                        ui.displayPosts(updatedPosts);
                        // Return to post selection - handled by caller
                    } else {
                        ui.displayInfo("No posts remaining. Returning to Posts Menu.");
                    }
                } else {
                    ui.displayError("Failed to refresh posts list: " + updatedPostsResult.getError());
                }
            } else {
                ui.displayError("Failed to delete post: " + result.getError());
            }
        } else {
            ui.displayInfo("Post deletion cancelled.");
        }
    }

    public void handleVotePost(Post existingPost) {
        String voteTypeInput = io.readLine("Enter vote type (UP/DOWN/NONE): ");
        if (voteTypeInput == null || voteTypeInput.trim().isEmpty()) {
            ui.displayError("Vote type cannot be empty.");
            return;
        }

        VoteType voteType = VoteType.fromString(voteTypeInput.trim());

        if (voteType == null) {
            ui.displayError("Invalid vote type. Please use UP, DOWN, or NONE.");
            return;
        }

        VoteRequestDto voteDto = new VoteRequestDto(voteType);
        ApiResult<Vote> result = postService.votePost(existingPost.id(), voteDto);

        if (result.isSuccess()) {
            ui.displaySuccess("Vote recorded successfully!");

            // un fel de reload ca sa se updateze
            ApiResult<Post> updatedPostResult = postService.getById(existingPost.id());
            if (updatedPostResult.isSuccess()) {
                Post updatedPost = updatedPostResult.getData();
                ui.displayInfo("Post updated with new vote:");
                ui.displayPost(updatedPost);
            }
        } else {
            ui.displayError("Failed to record vote: " + result.getError());
        }
    }

    public ApiResult<List<Post>> getAllPosts() {
        return postService.getAll();
    }

    public ApiResult<Post> getPostById(String id) {
        return postService.getById(id);
    }
}
