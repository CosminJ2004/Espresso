package presentation.handlers;

import infra.http.ApiResult;
import objects.domain.User;
import objects.dto.UserRequestDto;
import presentation.AppState;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.UserService;
import undo.UndoRedoManager;

public class AuthHandler {
    private final UserService userService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState = AppState.getInstance();

    public AuthHandler(UserService userService, ConsoleIO io, Renderer ui) {
        this.userService = userService;
        this.io = io;
        this.ui = ui;
    }

    public boolean handleLogin() {
        ui.displayInfo("Please enter your login credentials.");
        String username = io.readUsername();
        String password = io.readPassword();

        UserRequestDto loginDto = new UserRequestDto(username, password);
        ApiResult<User> result = userService.login(loginDto);

        if (result.isSuccess()) {
            User user = result.getData();
            appState.setCurrentUser(user);
            appState.setRunning(true);
            ui.displaySuccess("Welcome back, " + user.username() + "!");
            appState.setCurrentUser(user);
            return true;
        } else {
            ui.displayError("Login failed: " + result.getError());
            return false;
        }
    }

    public void handleRegister() {
        ui.displayInfo("Please enter you new account details:");
        String username = io.readUsername();
        String password = io.readPassword();

        UserRequestDto registerDto = new UserRequestDto(username, password);
        ApiResult<User> result = userService.create(registerDto);

        if (result.isSuccess()) {
            ui.displaySuccess(
                    "You have succesfully created your account: " + username + "!"
                            + "\nYou can now login.");
        } else {
            ui.displayError("Register failed: " + result.getError());
        }
    }

    public void handleLogout() {
        appState.setCurrentUser(null);
        //sterge toate actiunile de undo/redo ca le tin per sesiune
        UndoRedoManager.getInstance().clearAll();
        ui.displayInfo("You have been logged out.");
    }

    public void handleExit() {
        appState.setRunning(false);
    }
}
