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
                    refreshCommentsAndContinue();
                    return;
                case "2":
                    commentHandler.handleDeleteComment(selectedComment);
                    refreshCommentsAndContinue();
                    return;
                case "3":
                    commentHandler.handleVoteComment(selectedComment);
                    refreshCommentsAndContinue();
                    return;
                case "4":
                    commentHandler.handleReplyToComment(selectedComment, currentPost);
                    refreshCommentsAndContinue();
                    return;
                case "5":
                    //comment are replies
                    if (selectedComment.replies() != null && !selectedComment.replies().isEmpty()) {
                        handleNestedReplySelection(selectedComment, "1");
                    } else {
                        ui.displayInfo("This comment has no replies to interact with.");
                    }
                    return;
                case "6":
                    return; //Back
                default:
                    ui.displayInfo("Please enter a valid option (1-6).");
            }
        }
    }


    private void handleNestedReplySelection(Comment parentComment, String currentPath) {
        Comment freshParentComment = getFreshCommentById(parentComment.id());
        if (freshParentComment == null) {
            ui.displayError("Failed to refresh comment data.");
            return;
        }

        List<Comment> replies = freshParentComment.replies();
        if (replies == null || replies.isEmpty()) {
            ui.displayInfo("No replies to interact with.");
            return;
        }

        while (true) {
            ui.displayNestedReplySelectionMenu(freshParentComment, currentPath);
            String option = io.readLine("> ").trim();

            switch (option.trim()) {
                case "1":
                    selectNestedReplyForAction(replies, freshParentComment, currentPath);
                    break;
                case "2":
                    return; //Back
                default:
                    ui.displayInfo("Please enter a valid option (1-2).");
            }
        }
    }

    private void selectNestedReplyForAction(List<Comment> replies, Comment parentComment, String currentPath) {
        ui.displayNestedReplyList(replies, currentPath);
        String replyNumberInput = io.readLine("Enter reply number (1-" + replies.size() + "): ");
        if (replyNumberInput == null || replyNumberInput.trim().isEmpty()) {
            ui.displayError("Reply number cannot be empty.");
            return;
        }

        try {
            int replyNumber = Integer.parseInt(replyNumberInput.trim());
            if (replyNumber < 1 || replyNumber > replies.size()) {
                ui.displayError("Invalid reply number. Please enter a number between 1 and " + replies.size() + ".");
                return;
            }

            Comment selectedReply = replies.get(replyNumber - 1);
            String newPath = currentPath + "." + replyNumber;

            Comment freshSelectedReply = getFreshCommentById(selectedReply.id());
            if (freshSelectedReply == null) {
                ui.displayError("Failed to refresh reply data.");
                return;
            }
            
            ui.displayComment(freshSelectedReply);
            handleNestedReplyActionMenu(freshSelectedReply, parentComment, newPath);

        } catch (NumberFormatException e) {
            ui.displayError("Invalid number format. Please enter a valid number.");
        }
    }

    private void handleNestedReplyActionMenu(Comment selectedReply, Comment parentComment, String currentPath) {
        while (true) {
            ui.displayNestedReplyActionMenu(selectedReply, currentPath);
            String option = io.readLine("> ").trim();

            switch (option.trim()) {
                case "1":
                    commentHandler.handleEditComment(selectedReply);
                    refreshCommentsAndContinue();
                    return;
                case "2":
                    commentHandler.handleDeleteComment(selectedReply);
                    refreshCommentsAndContinue();
                    return;
                case "3":
                    commentHandler.handleVoteComment(selectedReply);
                    refreshCommentsAndContinue();
                    return;
                case "4":
                    commentHandler.handleReplyToComment(selectedReply, currentPost);
                    refreshCommentsAndContinue();
                    return;
                case "5":
                    if (selectedReply.replies() != null && !selectedReply.replies().isEmpty()) {
                        handleNestedReplySelection(selectedReply, currentPath);
                    } else {
                        ui.displayInfo("This reply has no nested replies to interact with.");
                    }
                    return;
                case "6":
                    return; // Back
                default:
                    ui.displayInfo("Please enter a valid option (1-6).");
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

            Comment selectedComment = getFreshCommentById(comments.get(commentNumber - 1).id());
            if (selectedComment == null) {
                ui.displayError("Failed to refresh comment data.");
                return;
            }

            ui.displayComment(selectedComment);
            handleCommentActionMenu(selectedComment);

        } catch (NumberFormatException e) {
            ui.displayError("Invalid number format. Please enter a valid number.");
        }
    }

    private void handleAddComment() {
        commentHandler.handleAddComment(currentPost);
    }
    private void refreshCommentsAndContinue() {
        ui.displayInfo("Refreshing comments...");
        commentHandler.handleViewComments(currentPost);
    }
    private Comment getFreshCommentById(String commentId) {
        ApiResult<List<Comment>> result = commentHandler.getCommentsByPostId(currentPost.id());
        if (!result.isSuccess() || result.getData() == null) {
            return null;
        }
        
        return findCommentById(result.getData(), commentId);
    }
    private Comment findCommentById(List<Comment> comments, String commentId) {
        for (Comment comment : comments) {
            if (comment.id().equals(commentId)) {
                return comment;
            }
            
            if (comment.replies() != null && !comment.replies().isEmpty()) {
                Comment found = findCommentById(comment.replies(), commentId);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
