import logger.FileLogger;
import logger.Log;
import logger.LogLevel;
import service.VoteService;
import service.CommentService;
import service.PostService;
import service.UserService;
import util.DB;


public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        VoteService voteService = new VoteService();
        PostService postService = new PostService(voteService);
        CommentService commentService = new CommentService(postService);

        Service service = new Service(userService, postService, commentService, voteService);
        Menu menu = new Menu(service);

        DB.CheckDBDriver();

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