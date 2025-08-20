package presentation;

import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.CommentService;
import service.FilterService;
import service.PostService;
import service.UserService;

public final class EspressoCLI implements AutoCloseable {
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final FilterService filterService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final MenuManager menuManager;
    private final AppState appState;

    public EspressoCLI(PostService postService, UserService userService, CommentService commentService, FilterService filterService, ConsoleIO io, Renderer ui) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.filterService = filterService;
        this.io = io;
        this.ui = ui;
        this.menuManager = new MenuManager(userService, postService, commentService, filterService, io, ui);
        this.appState = AppState.getInstance();
    }

    public void run() {
        try {
            ui.displayWelcomeBanner();
            // Login flow - meniul primary
            while (appState.isRunning()) {
                // Login flow - meniul primary
                if (!appState.isLoggedIn()) {
                    menuManager.runLoginMenu();
                    continue;
                }
                // dupa login trecem in run din MenuManager
                menuManager.run();
            }

            ui.displayGoodbyeBanner();

        } catch (Exception e) {
            ui.displayError("An unexpected error occurred: " + e.getMessage());
            ui.displayGoodbyeBanner();
        }
    }

    @Override
    public void close() throws Exception {
        io.close();
    }
}
