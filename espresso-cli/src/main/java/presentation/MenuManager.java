package presentation;

import objects.domain.User;
import objects.dto.UserRequestDto;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import presentation.views.PostMenuManager;
import presentation.views.UserMenuManager;
import service.PostService;
import service.UserService;
import infra.http.ApiResult;

public final class MenuManager {
    private final UserService userService;
    private final PostService postService;
    private final ConsoleIO io;
    private final Renderer ui;
    private final AppState appState;

    public MenuManager(UserService userService, PostService postService, ConsoleIO io, Renderer ui) {
        this.userService = userService;
        this.postService = postService;
        this.io = io;
        this.ui = ui;
        this.appState = AppState.getInstance();
    }

    public void run() {
        while(appState.isRunning() && appState.isLoggedIn()){
            ui.displayWelcomeMenu();
            String option = io.readLine("> ").trim();
            switch(option){
                case "1":
                    new UserMenuManager(userService, io , ui).run();
                    break;
                case "2": // View Posts
                    new PostMenuManager(postService, io, ui).run();
                    break;
                case "4": // Logout
                    appState.setCurrentUser(null);
                    ui.displayInfo("You have been logged out.");
                    return;
                case "5": //Exitt
                    handleExit();
                    return;
            }
        }
    }
    //TO DO: better separation between option input and field input
    public void runLoginMenu() {
        while (appState.isRunning() && !appState.isLoggedIn()) {
            ui.displayLoginMenu();
            String option = io.readLine("> ").trim();

            switch (option) {
                case "1": // Register
                    handleRegister();
                    break;

                case "2": // Login
                    if (handleLogin()) {
                        return; // logged in -> exit login menu
                    }
                    break;

                case "3": // Exit
                case "q":
                    handleExit();
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
            appState.setRunning(true);
            ui.displaySuccess("Welcome back, " + user.username() + "!");
            appState.setCurrentUser(user);
            return true;
            //runMainMenu();
        } else {
            ui.displayError("Login failed: " + result.getError());
            return false;
        }
    }

    private void handleRegister() {
        ui.displayInfo("Please enter you new account details:");
        String username = io.readUsername();
        String password = io.readPassword();

        UserRequestDto registerDto = new UserRequestDto(username, password);
        ApiResult<User> result = userService.create(registerDto);

        if (result.isSuccess()) { //dupa creare cnt, trebuie sa se logheze
            ui.displaySuccess(
                    "You have succesfully created your account: " + username + "!"
                            + "\nYou can now login.");
            //runMainMenu();
        } else {
            ui.displayError("Register failed: " + result.getError());
        }
    }

    private void handleExit(){
        appState.setRunning(false);
    }
}