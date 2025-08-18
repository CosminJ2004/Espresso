package presentation.menus;

import presentation.handlers.AuthHandler;
import presentation.handlers.UndoRedoHandler;
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
    private final UndoRedoHandler undoRedoHandler;

    public MainMenu(UserMenu userMenu, PostMenu postMenu, UndoRedoHandler undoRedoHandler, AuthHandler authHandler, ConsoleIO io, Renderer ui) {
        this.userMenu = userMenu;
        this.postMenu = postMenu;
        this.authHandler = authHandler;
        this.io = io;
        this.ui = ui;
        this.undoRedoHandler = undoRedoHandler;
    }

    public void run() {
        while (appState.isRunning() && appState.isLoggedIn()) {
            ui.displayWelcomeMenu();
            String option = io.readLine("> ").trim();
            switch (option) {
                case "1":
                    userMenu.run();
                    break;
                case "2": // Post Menu
                    postMenu.run();
                    break;
                case "3": // Undo/Redo Menu
                    handleUndoRedoMenu();
                    break;
                case "4": // Logout
                    authHandler.handleLogout();
                    return;
                case "5": //Exit
                    authHandler.handleExit();
                    return;
            }
        }
    }

    private void handleUndoRedoMenu() {
        while (true) {
            ui.displayUndoRedoMenu();
            String option = io.readLine("> ").trim();

            switch (option) {
                case "1":
                    undoRedoHandler.handleUndo();
                    break;
                case "2":
                    undoRedoHandler.handleRedo();
                    break;
                case "3":
                    undoRedoHandler.displayUndoRedoStatus();
                    break;
                case "4":
                    return; // Back to Main Menu
                default:
                    ui.displayInfo("Please enter a valid option (1-4).");
            }
        }
    }
}
