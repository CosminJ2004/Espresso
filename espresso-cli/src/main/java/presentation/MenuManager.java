package presentation;

import objects.domain.User;
import objects.dto.UserRequestDto;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import service.UserService;
import infra.http.ApiResult;

public final class MenuManager {
    private final UserService userService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState;

    public MenuManager(UserService userService, ConsoleIO io, Renderer ui) {
        this.userService = userService;
        this.io = io;
        this.ui = ui;
        this.appState = AppState.getInstance();
    }

    //TO DO: better separation between option input and field input
    public void runLoginMenu() {
        while (appState.isRunning() && !appState.isLoggedIn()) {
            ui.displayLoginMenu();
            String option = io.readLine("> ").trim();

            switch (option) {
                case "1": // Register
                    // handleRegister();
                    // ui.displayInfo("Register not implemented yet.");
                    break;

                case "2": // Login
                    if (handleLogin()) {
                        return; // logged in -> exit login menu
                    }
                    break;

                case "3": // Exit
                case "q":
                case "Q":
                    // handleExit(); sau appState.setRunning(false);
                    return;

                default:
                    ui.displayInfo("Invalid option. Please choose 1, 2 or 3.");
                    break;
            }
        }
    }

    private boolean handleLogin() {
        ui.displayInfo("Please enter your login credentials.");
        String username = io.readUsername();
        String password = io.readPassword();

        UserRequestDto loginDto = new UserRequestDto(username, password);
        ApiResult<User> result = userService.login(loginDto);

        if (result.isSuccess()) {
            User user = result.getData();
            appState.setCurrentUser(user);
            ui.displaySuccess("Welcome back, " + user.username() + "!");
            appState.setCurrentUser(user);
            return true;
            //runMainMenu();
        } else {
            ui.displayError("Login failed: " + result.getError());
            return false;
        }
    }
}