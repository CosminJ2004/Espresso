package presentation.views;

import infra.http.ApiResult;
import objects.domain.User;
import presentation.AppState;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.UserService;

public class UserMenuManager {
    private final UserService userService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public UserMenuManager(UserService userService, ConsoleIO io, Renderer ui) {
        this.userService = userService;
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
                    handleViewProfile();
                    break;
                case "2":
                    // Edit Profile
                    break;
                case "3":
                    // Search Users
                    break;
                case "4":
                    // Delete Account
                    break;
                case "5":
                    // Logout sau back cv de genu
                    stay = false;
                    break;
                default:
                    ui.displayInfo("Invalid option.");
            }
        }
    }
    private void handleViewProfile() {
        User current = appState.getCurrentUser();
        if (current == null) {
            ui.displayError("No active session. Please login first.");
            return;
        }

        String username = current.username();
        ApiResult<User> result = userService.findByUsername(username);

        if (result != null && result.isSuccess() && result.getData() != null) {
            User fresh = result.getData();
            ui.displayUserProfile(fresh);
            appState.setCurrentUser(fresh);
        } else {
            ui.displayError("Could not load profile. Please try again later.");
        }
    }
}
