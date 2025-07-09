package logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLogger implements ILogger {
    private LogLevel minLevel;
    private String filePath;

    public FileLogger(LogLevel minLevel, String filePath) {
        this.minLevel = minLevel;
        this.filePath = filePath;
    }

    @Override
    public void log(LogLevel level, String message) {
        if (level.ordinal() >= minLevel.ordinal()) {
            try (FileWriter fw = new FileWriter(filePath, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                out.println("[" + level + "] " + message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
