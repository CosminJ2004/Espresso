package infra.util;

public class AppConstants {
    private AppConstants() {
    }

    //folosit pentru Duration.ofSeconds care asteapta un long, si e folosit in ApiClient pt timeout
    public static final long DEFAULT_TIMEOUT_SECONDS = 10L;
    public static final int MAX_WIDTH = 60;
}
