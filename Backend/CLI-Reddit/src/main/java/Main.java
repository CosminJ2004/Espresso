import logger.FileLogger;
import logger.Log;
import logger.LogLevel;
import service.*;
import util.Database;

public class Main {
    public static void main(String[] args) {
        UserService userService = UserService.getInstance();
        VoteService voteService = VoteService.getInstance();
        PostService postService = PostService.getInstance();
        CommentService commentService = CommentService.getInstance();

        MenuService menuService = new MenuService(userService, postService, commentService, voteService);

        Database.CheckDriver();

        Log.RegisterLogger(new FileLogger(LogLevel.DEBUG,"DebugLogs.txt"));
        Log.RegisterLogger(new FileLogger(LogLevel.ERROR, "ErrorsLogs.txt"));

        Log.log(LogLevel.DEBUG,"Debug Message");
        Log.log(LogLevel.WARN,"Debug Message");
        Log.log(LogLevel.ERROR,"Error Message");


        AnimationService.showStartupAnimation();

        while (true) {
            menuService.createLoginMenu();
            menuService.createMainMenu();
        }

    }
}