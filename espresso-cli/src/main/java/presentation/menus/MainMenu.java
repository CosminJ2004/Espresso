package presentation.menus;

import presentation.handlers.AuthHandler;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import presentation.AppState;

public class MainMenu {
    private final UserMenu userMenu;
    private final PostMenu postMenu;
    private final AuthHandler authHandler;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public MainMenu(UserMenu userMenu, PostMenu postMenu, AuthHandler authHandler, ConsoleIO io, Renderer ui) {
        this.userMenu = userMenu;
        this.postMenu = postMenu;
        this.authHandler = authHandler;
        this.io = io;
        this.ui = ui;
    }

    public void run() {
        while(appState.isRunning() && appState.isLoggedIn()){
            ui.displayWelcomeMenu();
            String option = io.readLine("> ").trim();
            switch(option){
                case "1":
                    userMenu.run();
                    break;
                case "2": // Post Menu
                    postMenu.run();
                    break;
                case "3": // Logout
                    authHandler.handleLogout();
                    return;
                case "4": //Exit
                    authHandler.handleExit();
                    return;
            }
        }
    }
}
