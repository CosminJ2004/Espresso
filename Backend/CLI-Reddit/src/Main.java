import logger.FileLogger;
import logger.Log;
import logger.LogLevel;
import util.Console;

public class Main {
    public static void main(String[] args) {
        /*
        Service service = new Service();
        Menu menu = new Menu(service);
        DB.CheckDBDriver();
        */
        Log.RegisterLogger(new FileLogger(LogLevel.DEBUG,"DebugLogs.txt"));
        Log.RegisterLogger(new FileLogger(LogLevel.ERROR, "ErrorsLogs.txt"));

        Log.log(LogLevel.DEBUG,"Debug Message");
        Log.log(LogLevel.WARN,"Debug Message");
        Log.log(LogLevel.ERROR,"Error Message");

        /*while (true) {
            menu.createLoginMenu();
            menu.createMainMenu();
        }*/
    }
}