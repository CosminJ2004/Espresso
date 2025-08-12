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

    public EspressoCLI(PostService postService, UserService userService, CommentService commentService, ConsoleIO io, Renderer ui) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.io = io;
        this.ui = ui;
    }

    public void run(){
        //TO DO

        System.out.println("Espresso CLI merge blana...");
    }

    @Override
    public void close() throws Exception {
        io.close();
    }
}
