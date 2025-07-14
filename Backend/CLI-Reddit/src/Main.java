import logger.FileLogger;
import logger.Log;
import logger.LogLevel;
import util.DB;

public class Main {
    public static void main(String[] args) {
        Service service = new Service();
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