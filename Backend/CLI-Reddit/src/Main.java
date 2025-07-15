import logger.FileLogger;
import logger.Log;
import logger.LogLevel;
import service.VoteService;
import service.CommentService;
import service.PostService;
import service.UserService;
import util.Database;

public class Main {
    public static void main(String[] args) {
        UserService userService = UserService.getInstance();
        VoteService voteService = VoteService.getInstance();
        PostService postService = PostService.getInstance();
        CommentService commentService = CommentService.getInstance();

        Service service = new Service(userService, postService, commentService, voteService);
        Menu menu = new Menu(service);

        Database.CheckDriver();

        Log.RegisterLogger(new FileLogger(LogLevel.DEBUG,"DebugLogs.txt"));
        Log.RegisterLogger(new FileLogger(LogLevel.ERROR, "ErrorsLogs.txt"));

        Log.log(LogLevel.DEBUG,"Debug Message");
        Log.log(LogLevel.WARN,"Debug Message");
        Log.log(LogLevel.ERROR,"Error Message");

        while (true) {
            menu.createLoginMenu();
            menu.createMainMenu();
        }
    }
}