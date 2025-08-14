package presentation.menus;

import objects.domain.Comment;
import objects.domain.Post;
import presentation.handlers.CommentHandler;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import presentation.AppState;
import infra.http.ApiResult;

import java.util.List;

public class CommentMenu {
    private final CommentHandler commentHandler;
    private final ConsoleIO io;
    private final Renderer ui;
    private final Post currentPost;
    private final AppState appState = AppState.getInstance();

    public CommentMenu(CommentHandler commentHandler, ConsoleIO io, Renderer ui, Post currentPost) {
        this.commentHandler = commentHandler;
        this.io = io;
        this.ui = ui;
        this.currentPost = currentPost;
    }

    public void run() {
        boolean stay = true;
        while (stay && appState.isRunning() && appState.isLoggedIn()) {
            ui.displayCommentMenu(currentPost);
            String option = io.readLine("> ").trim();
            switch (option.trim()) {
                case "1":
                    handleViewComments();
                    break;
                case "2":
                    handleAddComment();
                    break;
                case "3":
                    return; // Back to Post Actions
                default:
                    ui.displayInfo("Please enter a valid option (1-3).");
            }
        }
    }

    private void handleViewComments() {
        commentHandler.handleViewComments(currentPost);
        handleCommentSelection();
    }

    private void handleCommentSelection() {
        ApiResult<List<Comment>> result = commentHandler.getCommentsByPostId(currentPost.id());
        if (!result.isSuccess() || result.getData() == null || result.getData().isEmpty()) {
            return;
        }

        List<Comment> comments = result.getData();
        while (true) {
            ui.displayCommentSelectionMenu();
            String option = io.readLine("> ").trim();

            switch (option.trim()) {
                case "1":
                    selectCommentForAction(comments);
                    break;
                case "2":
                    return; // Back to Comment Menu
                default:
                    ui.displayInfo("Please enter a valid option (1-2).");
            }
        }
    }

    private void handleCommentActionMenu(Comment selectedComment) {
        while (true) {
            ui.displayCommentActionMenu(selectedComment);
            String option = io.readLine("> ").trim();

            switch (option.trim()) {
                case "1":
                    commentHandler.handleEditComment(selectedComment);
                    return;
                case "2":
                    commentHandler.handleDeleteComment(selectedComment);
                    return;
                case "3":
                    commentHandler.handleVoteComment(selectedComment);
                    return;
                case "4":
                    commentHandler.handleReplyToComment(selectedComment, currentPost);
                    return;
                case "5":
                    return; // Back to comment selection
                default:
                    ui.displayInfo("Please enter a valid option (1-5).");
            }
        }
    }

    private void selectCommentForAction(List<Comment> comments) {
        String commentNumberInput = io.readLine("Enter comment number (1-" + comments.size() + "): ");
        if (commentNumberInput == null || commentNumberInput.trim().isEmpty()) {
            ui.displayError("Comment number cannot be empty.");
            return;
        }

        try {
            int commentNumber = Integer.parseInt(commentNumberInput.trim());
            if (commentNumber < 1 || commentNumber > comments.size()) {
                ui.displayError("Invalid comment number. Please enter a number between 1 and " + comments.size() + ".");
                return;
            }

            Comment selectedComment = comments.get(commentNumber - 1);
            ui.displayComment(selectedComment);
            handleCommentActionMenu(selectedComment);

        } catch (NumberFormatException e) {
            ui.displayError("Invalid number format. Please enter a valid number.");
        }
    }

    private void handleAddComment() {
        commentHandler.handleAddComment(currentPost);
    }
}
