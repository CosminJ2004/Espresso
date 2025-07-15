import logger.FileLogger;
import logger.Log;
import logger.LogLevel;
import util.Console;
import service.CommentService;
import service.PostService;
import service.UserService;
import util.DB;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        UserService userService=new UserService();
        PostService postService=new PostService();
        CommentService commentService=new CommentService();
        Service service = new Service(userService,postService,commentService);
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