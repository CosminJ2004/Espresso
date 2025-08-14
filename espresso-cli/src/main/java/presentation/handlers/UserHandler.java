package presentation.handlers;

import infra.http.ApiResult;
import objects.domain.User;
import objects.dto.UserRequestDto;
import presentation.AppState;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.UserService;

public class UserHandler {
    private final UserService userService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public UserHandler(UserService userService, ConsoleIO io, Renderer ui) {
        this.userService = userService;
        this.io = io;
        this.ui = ui;
    }

    public void handleViewProfile() {
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

    public void handleEditProfile() {
        User current = appState.getCurrentUser();
        if (current == null) {
            ui.displayError("No active session. Please login first.");
            return;
        }
        String currentUsername = current.username();

        ui.displayInfo("Please enter the following details to edit your profile:");
        String username = io.readUsername();
        String password = io.readPassword();

        UserRequestDto request = new UserRequestDto(username, password);
        ApiResult<User> result = userService.update(currentUsername, request);

        if (result.isSuccess()) {
            User user = result.getData();
            appState.setCurrentUser(user);
            appState.setRunning(true);
            ui.displaySuccess("You have successfully updated your profile: " + user.username() + "!");
            appState.setCurrentUser(user);
        } else {
            ui.displayError("Profile update failed: " + result.getError());
        }
    }

    public void handleDeleteAccount() {
        User current = appState.getCurrentUser();
        if (current == null) {
            ui.displayError("No active session. Please login first.");
            return;
        }
        String currentUsername = current.username();

        ApiResult<Void> result = userService.delete(currentUsername);

        if (result.isSuccess()) {
            appState.logout();
            ui.displaySuccess("Your account has been successfully deleted.");
        } else {
            ui.displayError("Account deletion failed: " + result.getError());
        }
    }

    public void handleSearchUsers() {
        ui.displayInfo("Please enter the username you want to search for:");
        String username = io.readUsername();
        ApiResult<User> result = userService.findByUsername(username);

        if (result.isSuccess() && result.getData() != null) {
            User user = result.getData();
            ui.displayUserProfile(user);
        } else {
            ui.displayError("User not found or search failed: " + result.getError());
        }
    }
}
