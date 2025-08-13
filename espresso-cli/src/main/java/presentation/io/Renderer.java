package presentation.io;


import infra.ui.Colors;
import objects.domain.Post;
import objects.domain.User;
import presentation.io.outputLayout.BoxRenderer;

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
                        "1. Create Post without image",
                        "2. Create Post with image",
                        "2. View Posts",
                        "3. Edit Post",
                        "4. Delete Post",
                        "5. Vote Post"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayWelcomeMenu() {
        List<String> lines = box.buildBox(
                "MAIN MENU",
                List.of(
                        "Please select an option:",
                        "1. User Menu",
                        "2. Post Menu",
                        "3. Comment Menu",
                        "4. Logout",
                        "5. Exit"
                )
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toYellow(line)));
        }
        System.out.println();
    }

    public void displayCommentMenu() {
        List<String> lines = box.buildBox(
                "COMMENTS MENU",
                List.of(
                        "Please select an option:",
                        "1. Create Comment",
                        "2. View Comments",
                        "3. Edit Comment",
                        "4. Delete Comment",
                        "5. Vote Comment"
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
                        "ID: " + user.id(),
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
    public void displayPost(objects.domain.Post post) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String created = (post.createdAt() == null) ? "-" : post.createdAt().format(fmt);
        String updated = (post.updatedAt() == null) ? "-" : post.updatedAt().format(fmt);
        String img     = (post.imageUrl() == null || post.imageUrl().isBlank()) ? "-" : post.imageUrl();
        String filter  = (post.filter() == null) ? "-" : String.valueOf(post.filter());
        String vote    = (post.userVote() == null) ? "-" : post.userVote();

        List<String> lines = box.buildBox(
                "POST",
                List.of(
                        "Title: " + nvl(post.title()),
                        "Author: " + nvl(post.author()) + " | Subreddit: " + nvl(post.subreddit()),
                        "ID: " + post.id(),
                        "Upvotes: " + nvl(post.upvotes()) + " | Downvotes: " + nvl(post.downvotes()),
                        "Score: " + nvl(post.score()) + " | Comments: " + nvl(post.commentCount()),
                        "User vote: " + vote + " | Filter: " + filter,
                        "Image URL: " + img,
                        "Created: " + created + " | Updated: " + updated,
                        "",
                        nvl(post.content())
                )
        );

        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toBlue(line)));
        }
        System.out.println();
    }

    public void displayPosts(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            displayInfo("No posts yet. Be the first to create one!");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<String> body = new ArrayList<>();
        body.add("Total: " + posts.size());

        for (int i = 0; i < posts.size(); i++) {
            Post p = posts.get(i);

            String created = (p.createdAt() == null) ? "-" : p.createdAt().format(fmt);
            String preview = preview(nvl(p.content()), 100);
            String hasImage = (p.imageUrl() == null || p.imageUrl().isBlank()) ? "no" : "yes";

            body.add("#" + (i + 1) + "  " + nvl(p.title()));
            body.add("ID: " + p.id() + " | Subreddit: " + nvl(p.subreddit()));
            body.add("Author: " + nvl(p.author()) + " | Created: " + created);
            body.add("Score: " + nvl(p.score()) + " | Comments: " + nvl(p.commentCount())
                    + " | Image: " + hasImage + " | Vote: " + nvl(p.userVote()));
            if (!preview.equals("-")) {
                body.add(preview);
            }

            if (i < posts.size() - 1) body.add("—");
        }

        List<String> lines = box.buildBox("POSTS", body);
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toBlue(line)));
        }
        System.out.println();
    }

    private String preview(String s, int max) {
        if (s == null || s.isBlank()) return "-";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
    private String nvl(Object o) { return o == null ? "-" : String.valueOf(o); }
    private String nvl(String s) { return (s == null || s.isBlank()) ? "-" : s; }
}
