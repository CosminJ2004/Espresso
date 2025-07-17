package service;

public class AnimationService {


    public static void showStartupAnimation() {
        String logo = """
                 _____          _ _ _     _
                |  __ \\        | (_) |   | |
                | |__) |___  __| |_| |__ | |_
                |  _  // _ \\/ _` | | '_ \\| __|
                | | \\ \\  __/ (_| | | | | | |_
                |_|  \\_\\___|\\__,_|_|_| |_|\\__|

                """;

        System.out.println("Starting Reddit Clone...");
        try {
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.println();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(logo);
        System.out.println("Ready.\n");
    }

    public static void showGoodbyeAnimation() {
        String goodbye = """
         _____                 _ _                
        |  __ \\               | | |               
        | |  \\/ ___   ___   __| | |__  _   _  ___ 
        | | __ / _ \\ / _ \\ / _` | '_ \\| | | |/ _ \\
        | |_\\ \\ (_) | (_) | (_| | |_) | |_| |  __/
         \\____/\\___/ \\___/ \\__,_|_.__/ \\__, |\\___|
                                         __/ |    
                                        |___/     
        """;

        System.out.println("\nShutting down...");
        try {
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                Thread.sleep(400);
            }
            System.out.println("\n");
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(goodbye);
        System.out.println("See you soon! 👋\n");
    }

}
