package presentation.handlers;

import infra.http.ApiResult;
import objects.domain.Comment;
import objects.domain.Post;
import objects.domain.Vote;
import objects.domain.VoteType;
import objects.dto.CommentRequestDto;
import objects.dto.VoteRequestDto;
import presentation.AppState;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.CommentService;
import service.PostService;

import java.util.List;

public class CommentHandler {
    private final PostService postService;
    private final CommentService commentService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public CommentHandler(PostService postService, CommentService commentService, ConsoleIO io, Renderer ui) {
        this.postService = postService;
        this.commentService = commentService;
        this.io = io;
        this.ui = ui;
    }

    public void handleViewComments(Post currentPost) {
        ApiResult<List<Comment>> result = postService.getCommentsByPostId(currentPost.id());

        if (result == null) {
            ui.displayError("Unexpected null response from service.");
            return;
        }
        if (!result.isSuccess()) {
            ui.displayError(result.getError() != null ? result.getError() : "Failed to load comments.");
            return;
        }

        List<Comment> comments = result.getData();
        if (comments == null || comments.isEmpty()) {
            ui.displayInfo("No comments yet. Be the first to comment!");
            return;
        }

        ui.displayComments(comments);
    }

    public void handleAddComment(Post currentPost) {
        if (!appState.isLoggedIn()) {
            ui.displayError("You must be logged in to add a comment.");
            return;
        }

        String currentUsername = appState.getCurrentUser().username();
        String content = io.readLine("Enter comment content: ");

        if (content == null || content.trim().isEmpty()) {
            ui.displayError("Comment content cannot be empty.");
            return;
        }

        CommentRequestDto requestDto = new CommentRequestDto(content.trim(), currentUsername, null);
        ApiResult<Comment> result = postService.addComment(currentPost.id(), requestDto);

        if (result.isSuccess()) {
            ui.displaySuccess("Comment added successfully!");
            ui.displayComment(result.getData());
            // Refresh comments dupa add
            refreshComments(currentPost);
        } else {
            ui.displayError("Failed to add comment: " + result.getError());
        }
    }

    public void handleEditComment(Comment existingComment) {
        String currentUsername = appState.getCurrentUser().username();

        if (!existingComment.author().equals(currentUsername)) {
            ui.displayError("You can only edit your own comments.");
            return;
        }

        String newContent = io.readLine("Enter new content (or press Enter to keep current): ");

        if (newContent.trim().isEmpty()) {
            newContent = existingComment.content();
        }

        CommentRequestDto updateDto = new CommentRequestDto(newContent.trim(), currentUsername, existingComment.parentId());
        ApiResult<Comment> result = commentService.update(existingComment.id(), updateDto);

        if (result.isSuccess()) {
            ui.displaySuccess("Comment updated successfully!");
            ui.displayComment(result.getData());
            // Refresh comments dupa edit - handled by caller
        } else {
            ui.displayError("Failed to update comment: " + result.getError());
        }
    }

    public void handleDeleteComment(Comment existingComment) {
        String currentUsername = appState.getCurrentUser().username();

        if (!existingComment.author().equals(currentUsername)) {
            ui.displayError("You can only delete your own comments.");
            return;
        }

        String confirmation = io.readLine("Are you sure you want to delete this comment? (yes/no): ");
        if ("yes".equalsIgnoreCase(confirmation.trim())) {
            ApiResult<Void> result = commentService.delete(existingComment.id());
            if (result.isSuccess()) {
                String successMessage = result.getMessage() != null ? result.getMessage() : "Comment deleted successfully!";
                ui.displaySuccess(successMessage);
                // Refresh comments dupa delete - handled by caller
            } else {
                ui.displayError("Failed to delete comment: " + result.getError());
            }
        } else {
            ui.displayInfo("Comment deletion cancelled.");
        }
    }

    public void handleVoteComment(Comment existingComment) {
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
        ApiResult<Vote> result = commentService.voteComment(existingComment.id(), voteDto);

        if (result.isSuccess()) {
            ui.displaySuccess("Vote recorded successfully!");
            // Refresh comments dupa vot - handled by caller
        } else {
            ui.displayError("Failed to record vote: " + result.getError());
        }
    }

    public void handleReplyToComment(Comment parentComment, Post currentPost) {
        if (!appState.isLoggedIn()) {
            ui.displayError("You must be logged in to reply to a comment.");
            return;
        }

        String currentUsername = appState.getCurrentUser().username();
        String content = io.readLine("Enter reply content: ");

        if (content == null || content.trim().isEmpty()) {
            ui.displayError("Reply content cannot be empty.");
            return;
        }

        CommentRequestDto requestDto = new CommentRequestDto(content.trim(), currentUsername, parentComment.id());
        ApiResult<Comment> result = postService.addComment(currentPost.id(), requestDto);

        if (result.isSuccess()) {
            ui.displaySuccess("Reply added successfully!");
            ui.displayComment(result.getData());
            // Refresh comments dupa reply
            refreshComments(currentPost);
        } else {
            ui.displayError("Failed to add reply: " + result.getError());
        }
    }

    private void refreshComments(Post currentPost) {
        ui.displayInfo("Refreshing comments...");
        handleViewComments(currentPost);
    }

    public ApiResult<List<Comment>> getCommentsByPostId(String postId) {
        return postService.getCommentsByPostId(postId);
    }
}
