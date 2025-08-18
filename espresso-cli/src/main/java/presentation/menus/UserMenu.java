package presentation.menus;

import presentation.handlers.UserHandler;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import presentation.AppState;

public class UserMenu {
    private final UserHandler userHandler;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public UserMenu(UserHandler userHandler, ConsoleIO io, Renderer ui) {
        this.userHandler = userHandler;
        this.io = io;
        this.ui = ui;
    }

    public void run() {
        boolean stay = true;
        while (stay && appState.isRunning() && appState.isLoggedIn()) {
            ui.displayUserMenu();
            String option = io.readLine("> ").trim();
            switch (option.trim()) {
                case "1":
                    // View Profile
                    userHandler.handleViewProfile();
                    break;
                case "2":
                    // Edit Profile
                    userHandler.handleEditProfile();
                    break;
                case "3":
                    // Search Users
                    userHandler.handleSearchUsers();
                    break;
                case "4":
                    // Delete Account
                    userHandler.handleDeleteAccount();
                    break;
                case "5":
                    // Back to main menu
                    stay = false;
                    break;
                default:
                    ui.displayInfo("Invalid option.");
            }
        }
    }
}
