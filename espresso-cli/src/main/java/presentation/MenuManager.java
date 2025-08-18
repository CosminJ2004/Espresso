package presentation;

import presentation.handlers.*;
import presentation.menus.CommentMenu;
import presentation.menus.LoginMenu;
import presentation.menus.MainMenu;
import presentation.menus.PostMenu;
import presentation.menus.UserMenu;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.CommentService;
import service.PostService;
import service.UserService;

public final class MenuManager {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState;

    //handlers
    private final AuthHandler authHandler;
    private final UserHandler userHandler;
    private final PostHandler postHandler;
    private final CommentHandler commentHandler;
    private final UndoRedoHandler undoRedoHandler;
    //menirui
    private final LoginMenu loginMenu;
    private final MainMenu mainMenu;

    public MenuManager(UserService userService, PostService postService, CommentService commentService, ConsoleIO io, Renderer ui) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.io = io;
        this.ui = ui;
        this.appState = AppState.getInstance();

        this.authHandler = new AuthHandler(userService, io, ui);
        this.userHandler = new UserHandler(userService, io, ui);
        this.postHandler = new PostHandler(postService, io, ui);
        this.commentHandler = new CommentHandler(postService, commentService, io, ui);
        this.undoRedoHandler = new UndoRedoHandler(io, ui);

        UserMenu userMenu = new UserMenu(userHandler, io, ui);
        PostMenu postMenu = new PostMenu(postHandler, commentHandler, io, ui);
        
        this.loginMenu = new LoginMenu(authHandler, io, ui);
        this.mainMenu = new MainMenu(userMenu, postMenu, undoRedoHandler, authHandler, io, ui);
    }

    public void run() {
        mainMenu.run();
    }

    public void runLoginMenu() {
        loginMenu.run();
    }
}