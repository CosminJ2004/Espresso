import logger.FileLogger;
import logger.Log;
import logger.LogLevel;
import service.CommentService;
import service.PostService;
import service.UserService;
import util.DB;

public class Main {
    public static void main(String[] args) {
        UserService userService=new UserService();
        PostService postService=new PostService();
        CommentService commentService=new CommentService();
        Service service = new Service(userService,postService,commentService);
        Menu menu = new Menu(service);

        DB.CheckDBDriver();


        // Log.InitLoggingSystem();
        FileLogger mahFileLogger = new FileLogger(LogLevel.DEBUG,"superLog1.txt");
        FileLogger errLogger = new FileLogger(LogLevel.ERROR, "errors.txt");

        Log.RegisterLogger(mahFileLogger);
        Log.RegisterLogger(errLogger);

        Log.log(LogLevel.ERROR,"Hello from main");

        while (true) {
            menu.createLoginMenu();
            menu.createMainMenu();
        }
    }
}