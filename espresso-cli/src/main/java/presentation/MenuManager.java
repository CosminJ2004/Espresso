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

   //TO DO
}
