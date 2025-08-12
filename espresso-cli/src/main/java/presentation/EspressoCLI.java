package presentation;

import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.CommentService;
import service.PostService;
import service.UserService;

public final class EspressoCLI implements AutoCloseable{
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final MenuManager menuManager;
    private final AppState appState;

    public EspressoCLI(PostService postService, UserService userService, CommentService commentService, ConsoleIO io, Renderer ui) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.io = io;
        this.ui = ui;
        this.menuManager = new MenuManager(userService, io, ui);
        this.appState = AppState.getInstance();
    }

    public void run(){
        try {
            ui.displayWelcomeBanner();
            //menuManager.runLoginMenu();
            
            if (!appState.isRunning()) {
                //ui.displayGoodbyeBanner();
            }
        } catch (Exception e) {
            //ui.displayError("An unexpected error occurred: " + e.getMessage());
            //ui.displayGoodbyeBanner();
        }
    }

    @Override
    public void close() throws Exception {
        io.close();
    }
}
