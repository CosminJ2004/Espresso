package presentation.io;

import infra.ui.Colors;
import objects.domain.Comment;
import objects.domain.Post;
import objects.domain.User;
import presentation.io.outputLayout.BoxRenderer;
import presentation.io.outputLayout.TextLayout;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final BoxRenderer box = new BoxRenderer();

    public void displayWelcomeBanner() {
        List<String> lines = box.buildBox(
                "Espresso CLI",
                List.of("Welcome to Espresso! Easy-to-use & interactive CLI.")
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toOrange(line)));
        }
        System.out.println();
    }

    public void displayGoodbyeBanner() {
        List<String> lines = box.buildBox(
                "Goodbye!",
                List.of("Thank you for using Espresso CLI. See you next time!")
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toOrange(line)));
        }
        System.out.println();
    }

    public void displayLoginMenu() {
        List<String> lines = box.buildBox(
                "SIGN-IN MENU",
                List.of(
                        "Please select an option:",
                        "1. Register",
                        "2. Login",
                        "3. Exit"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayUserMenu() {
        List<String> lines = box.buildBox(
                "USER MENU",
                List.of(
                        "Please select an option:",
                        "1. View Profile",
                        "2. Edit Profile",
                        "3. Search Users",
                        "4. Delete Account",
                        "5. Back to Main Menu"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayPostMenu() {
        List<String> lines = box.buildBox(
                "POSTS MENU",
                List.of(
                        "Please select an option:",
                        "1. Create Post",
                        "2. View Posts",
                        "3. Back to Main Menu"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    //varianta veche, inainte de undo/redo
//    public void displayWelcomeMenu() {
//        List<String> lines = box.buildBox(
//                "MAIN MENU",
//                List.of(
//                        "Please select an option:",
//                        "1. User Menu",
//                        "2. Post Menu",
//                        //"3. Comment Menu",
//                        "3. Logout",
//                        "4. Exit"
//                )
//        );
//        for (String line : lines) {
//            System.out.println(Colors.toBold(Colors.toYellow(line)));
//        }
//        System.out.println();
//    }
    public void displayWelcomeMenu() {
        List<String> lines = box.buildBox(
                "MAIN MENU",
                List.of(
                        "Please select an option:",
                        "1. User Menu",
                        "2. Post Menu",
                        "3. Undo/Redo",
                        "4. Logout",
                        "5. Exit"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayUndoRedoMenu() {
        List<String> lines = box.buildBox(
                "UNDO/REDO MENU",
                List.of(
                        "Please select an option:",
                        "1. Undo last operation",
                        "2. Redo last operation",
                        "3. Show undo/redo status",
                        "4. Back to Main Menu"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayError(String message) {
        List<String> lines = box.buildBox("[ERROR]", List.of(message));
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toRed(line)));
        }
        System.out.println();
    }

    public void displaySuccess(String message) {
        List<String> lines = box.buildBox("[SUCCESS]", List.of(message));
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toGreen(line)));
        }
        System.out.println();
    }

    public void displayInfo(String message) {
        List<String> lines = box.buildBox("[INFO]", List.of(message));
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toBlue(line)));
        }
        System.out.println();
    }

    public void displayImage(String url) {
        final String title = "IMAGE";
        final String label = "Image URL: ";
        final String line = label + url;

        int inner = Math.max(title.length(), line.length());
        BoxRenderer tight = new BoxRenderer(inner);
        String bodyLine = line;

        List<String> lines = tight.buildBox(title, List.of(bodyLine));

        for (String l : lines) {
            System.out.println(Colors.toBold(Colors.toCyan(l)));
        }
        System.out.println();
    }

    public void displayInputPrompt(String prompt) {
        System.out.print(Colors.toBold(Colors.toYellow(prompt)));
    }

    //displayers
    //user entity
    public void displayUserProfile(User user) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String created = (user.createdAt() == null) ? "-" : user.createdAt().format(fmt);

        List<String> lines = box.buildBox(
                "PROFILE",
                List.of(
                        "Username: " + user.username(),
                        //"ID: " + user.id(),
                        "Created: " + created,
                        "Posts: " + user.postCount(),
                        "Comments: " + user.commentCount()
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toBlue(line)));
        }
        System.out.println();
    }

    //posts
    public void displayPost(Post post) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String created = (post.createdAt() == null) ? "-" : post.createdAt().format(fmt);
        String updated = (post.updatedAt() == null) ? "-" : post.updatedAt().format(fmt);
        String img = (post.imageUrl() == null || post.imageUrl().isBlank()) ? "-" : post.imageUrl();
        String filter = (post.filter() == null) ? "-" : String.valueOf(post.filter());
        String vote = (post.userVote() == null) ? "-" : post.userVote().getValue();

        List<String> lines = box.buildBox(
                "POST",
                List.of(
                        "Title: " + nvl(post.title()),
                        "Author: " + nvl(post.author()) + " | Subreddit: " + nvl(post.subreddit()),
                        //"ID: " + post.id(),
                        "Upvotes: " + nvl(post.upvotes()) + " | Downvotes: " + nvl(post.downvotes()),
                        "Score: " + nvl(post.score()) + " | Comments: " + nvl(post.commentCount()),
                        "User vote: " + vote + " | Filter: " + filter,
                        //"Image URL: " + img,
                        "Created: " + created + " | Updated: " + updated,
                        "",
                        nvl(post.content())
                )
        );

        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toBrightWhite(line)));
        }
        if(!img.equals("-")) {
            displayImage(img);
        }
        System.out.println();
    }

    public void displayPosts(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            displayInfo("No posts yet. Be the first to create one!");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < posts.size(); i++) {
            Post p = posts.get(i);
            displayPostInContainer(p, i + 1, fmt);
            System.out.println();
        }
    }

    private void displayPostInContainer(Post post, int postNumber, DateTimeFormatter fmt) {
        String created = (post.createdAt() == null) ? "-" : post.createdAt().format(fmt);
        String preview = preview(nvl(post.content()), 80);
        String hasImage = (post.imageUrl() == null || post.imageUrl().isBlank()) ? "no" : "yes";

        List<String> body = new ArrayList<>();
        body.add("Title: " + nvl(post.title()));
        body.add("Author: " + nvl(post.author()) + " | Subreddit: " + nvl(post.subreddit()));
        body.add("Score: " + nvl(post.score()) + " | Comments: " + nvl(post.commentCount()) + " | Image: " + hasImage);
        body.add("Created: " + created + " | Vote: " + nvl(post.userVote()));
        if (!preview.equals("-")) {
            body.add("Content: " + preview);
        }

        List<String> lines = box.buildBox("POST #" + postNumber, body);
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toBrightWhite(line)));
        }

        if(hasImage.equals("yes")){
            displayImage(post.imageUrl());
        }
    }

    private String preview(String s, int max) {
        if (s == null || s.isBlank()) return "-";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private String nvl(Object o) {
        return o == null ? "-" : String.valueOf(o);
    }

    private String nvl(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }

    public void displayPostSelectionMenu() {
        List<String> lines = box.buildBox(
                "POST SELECTION",
                List.of(
                        "Please select an option:",
                        "1. Select a post",
                        "2. Back to Posts Menu"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayPostActionMenu(Post post) {
        List<String> lines = box.buildBox(
                "POST ACTIONS - " + nvl(post.title()),
                List.of(
                        "Please select an option:",
                        "1. Edit Post",
                        "2. Delete Post",
                        "3. Vote Post",
                        "4. Comment Section",
                        "5. Back to post selection"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayCommentMenu(Post post) {
        List<String> lines = box.buildBox(
                "COMMENT SECTION - " + nvl(post.title()),
                List.of(
                        "Please select an option:",
                        "1. View Comments",
                        "2. Add Comment",
                        "3. Back to Post Actions"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayCommentSelectionMenu() {
        List<String> lines = box.buildBox(
                "COMMENT SELECTION",
                List.of(
                        "Please select an option:",
                        "1. Select a comment",
                        "2. Back to Comment Menu"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayCommentActionMenu(Comment comment) {
        List<String> lines = box.buildBox(
                "COMMENT ACTIONS - " + nvl(comment.content()),
                List.of(
                        "Please select an option:",
                        "1. Edit Comment",
                        "2. Delete Comment",
                        "3. Vote Comment",
                        "4. Reply to Comment",
                        "5. Interact with Replies",
                        "6. Back to comment selection"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayNestedReplySelectionMenu(Comment parentComment, String currentPath) {
        String preview = preview(nvl(parentComment.content()), 40);
        List<String> lines = box.buildBox(
                "REPLY SELECTION - Level " + currentPath + " - " + preview,
                List.of(
                        "Please select an option:",
                        "1. Select a reply to interact with",
                        "2. Back to parent actions"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayNestedReplyActionMenu(Comment reply, String currentPath) {
        String preview = preview(nvl(reply.content()), 40);
        List<String> lines = box.buildBox(
                "REPLY ACTIONS - Level " + currentPath + " - " + preview,
                List.of(
                        "Please select an option:",
                        "1. Edit Reply",
                        "2. Delete Reply",
                        "3. Vote Reply",
                        "4. Reply to Reply",
                        "5. Interact with Nested Replies",
                        "6. Back to parent reply selection"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayNestedReplyList(List<Comment> replies, String currentPath) {
        if (replies == null || replies.isEmpty()) {
            displayInfo("No replies available at this level.");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        displayInfo("Available replies at level " + currentPath + ":");
        
        for (int i = 0; i < replies.size(); i++) {
            Comment reply = replies.get(i);
            String created = (reply.createdAt() == null) ? "-" : reply.createdAt().format(fmt);
            String preview = preview(nvl(reply.content()), 50);
            String nestedPath = currentPath + "." + (i + 1);

            List<String> body = new ArrayList<>();
            body.add("- Reply #" + nestedPath + " by: " + nvl(reply.author()));
            body.add("Score: " + nvl(reply.score()) + " | Upvotes: " + nvl(reply.upvotes()) + " | Downvotes: " + nvl(reply.downvotes()));
            body.add("Created: " + created);
            if (!preview.equals("-")) {
                body.add("Content: " + preview);
            }
            if (reply.replies() != null && !reply.replies().isEmpty()) {
                body.add("Has " + reply.replies().size() + " nested replies");
            }

            List<String> lines = box.buildBox("REPLY #" + nestedPath, body);
            for (String line : lines) {
                System.out.println(Colors.toBold(Colors.toBlue(line)));
            }
            System.out.println();
        }
    }

    public void displayComments(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            displayInfo("No comments yet. Be the first to comment!");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < comments.size(); i++) {
            Comment c = comments.get(i);
            displayCommentInContainer(c, i + 1, fmt);
            System.out.println();
        }
    }

    private void displayReplies(List<Comment> replies, int parentCommentNumber, DateTimeFormatter fmt) {
        for (int i = 0; i < replies.size(); i++) {
            Comment reply = replies.get(i);
            String created = (reply.createdAt() == null) ? "-" : reply.createdAt().format(fmt);
            String preview = preview(nvl(reply.content()), 50);

            List<String> body = new ArrayList<>();
            body.add("- Reply by: " + nvl(reply.author()));
            body.add("Score: " + nvl(reply.score()) + " | Upvotes: " + nvl(reply.upvotes()) + " | Downvotes: " + nvl(reply.downvotes()));
            body.add("Created: " + created);
            if (!preview.equals("-")) {
                body.add("Content: " + preview);
            }

            String replyNumber = parentCommentNumber > 0 ? parentCommentNumber + "." + (i + 1) : String.valueOf(i + 1);
            List<String> lines = box.buildBox("REPLY #" + replyNumber, body);
            for (String line : lines) {
                System.out.println(Colors.toBold(Colors.toBlue(line)));
            }
            System.out.println();

            if (reply.replies() != null && !reply.replies().isEmpty()) {
                displayRepliesNested(reply.replies(), replyNumber, fmt);
            }
        }
    }

    private void displayRepliesNested(List<Comment> replies, String parentReplyNumber, DateTimeFormatter fmt) {
        for (int i = 0; i < replies.size(); i++) {
            Comment reply = replies.get(i);
            String created = (reply.createdAt() == null) ? "-" : reply.createdAt().format(fmt);
            String preview = preview(nvl(reply.content()), 50);

            List<String> body = new ArrayList<>();
            body.add("- Reply by: " + nvl(reply.author()));
            body.add("Score: " + nvl(reply.score()) + " | Upvotes: " + nvl(reply.upvotes()) + " | Downvotes: " + nvl(reply.downvotes()));
            body.add("Created: " + created);
            if (!preview.equals("-")) {
                body.add("Content: " + preview);
            }

            String nestedReplyNumber = parentReplyNumber + "." + (i + 1);
            List<String> lines = box.buildBox("REPLY #" + nestedReplyNumber, body);
            for (String line : lines) {
                System.out.println(Colors.toBold(Colors.toCyan(line)));
            }
            System.out.println();

            if (reply.replies() != null && !reply.replies().isEmpty()) {
                displayRepliesNested(reply.replies(), nestedReplyNumber, fmt);
            }
        }
    }

    private void displayCommentInContainer(Comment comment, int commentNumber, DateTimeFormatter fmt) {
        String created = (comment.createdAt() == null) ? "-" : comment.createdAt().format(fmt);
        String preview = preview(nvl(comment.content()), 60);
        String isReply = (comment.parentId() != null) ? " (Reply)" : "";

        List<String> body = new ArrayList<>();
        body.add("Author: " + nvl(comment.author()) + isReply);
        body.add("Score: " + nvl(comment.score()) + " | Upvotes: " + nvl(comment.upvotes()) + " | Downvotes: " + nvl(comment.downvotes()));
        body.add("Created: " + created);
        if (!preview.equals("-")) {
            body.add("Content: " + preview);
        }

        List<String> lines = box.buildBox("COMMENT #" + commentNumber, body);
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toGreen(line)));
        }

        if (comment.replies() != null && !comment.replies().isEmpty()) {
            displayReplies(comment.replies(), commentNumber, fmt);
        }
    }

    public void displayComment(Comment comment) {
        String created = (comment.createdAt() == null) ? "-" : comment.createdAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String updated = (comment.updatedAt() == null) ? "-" : comment.updatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String isReply = (comment.parentId() != null) ? " (Reply to comment " + comment.parentId() + ")" : "";

        List<String> lines = box.buildBox(
                "COMMENT",
                List.of(
                        "Author: " + nvl(comment.author()) + isReply,
                        "Score: " + nvl(comment.score()) + " | Upvotes: " + nvl(comment.upvotes()) + " | Downvotes: " + nvl(comment.downvotes()),
                        "Created: " + created + " | Updated: " + updated,
                        "",
                        nvl(comment.content())
                )
        );

        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toGreen(line)));
        }
        System.out.println();

        if (comment.replies() != null && !comment.replies().isEmpty()) {
            displayReplies(comment.replies(), 0, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); // 0 pt comentariu singur
        }
    }

    public void displayFilters(List<objects.domain.Filter> filters) {
        if (filters == null || filters.isEmpty()) {
            displayInfo("No filters available.");
            return;
        }

        List<String> filterOptions = new ArrayList<>();
        for (objects.domain.Filter filter : filters) {
            filterOptions.add(filter.getId() + ". " + filter.getLabel());
        }

        List<String> lines = box.buildBox("OPTIONS", filterOptions);
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toCyan(line)));
        }
        System.out.println();
    }
}
