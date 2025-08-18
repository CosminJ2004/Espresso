package presentation.menus;

import presentation.handlers.AuthHandler;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import presentation.AppState;

public class LoginMenu {
    private final AuthHandler authHandler;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public LoginMenu(AuthHandler authHandler, ConsoleIO io, Renderer ui) {
        this.authHandler = authHandler;
        this.io = io;
        this.ui = ui;
    }

    public void run() {
        while (appState.isRunning() && !appState.isLoggedIn()) {
            ui.displayLoginMenu();
            String option = io.readLine("> ").trim();

            switch (option) {
                case "1": // Register
                    authHandler.handleRegister();
                    break;

                case "2": // Login
                    if (authHandler.handleLogin()) {
                        return; // logged in -> exit login menu
                    }
                    break;

                case "3": // Exit
                case "q":
                    authHandler.handleExit();
                    return;

                default:
                    ui.displayInfo("Invalid option. Please choose 1, 2 or 3.");
                    break;
            }
        }
    }
}
