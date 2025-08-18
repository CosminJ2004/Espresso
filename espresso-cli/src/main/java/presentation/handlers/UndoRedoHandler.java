package presentation.handlers;

import undo.UndoRedoManager;
import undo.action.UndoableAction;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;

public class UndoRedoHandler {
    private final UndoRedoManager undoRedoManager;
    private final ConsoleIO io;
    private final Renderer ui;

    public UndoRedoHandler(ConsoleIO io, Renderer ui) {
        this.undoRedoManager = UndoRedoManager.getInstance();
        this.io = io;
        this.ui = ui;
    }

    public void handleUndo() {
        if (!undoRedoManager.canUndo()) {
            ui.displayInfo("Nothing to undo.");
            return;
        }

        try {
            UndoableAction operation = undoRedoManager.undo();
            if (operation != null) {
                ui.displaySuccess("Operation undone successfully!");
            } else {
                ui.displayError("Failed to undo operation.");
            }
        } catch (Exception e) {
            ui.displayError("Error during undo: " + e.getMessage());
        }
    }

    public void handleRedo() {
        if (!undoRedoManager.canRedo()) {
            ui.displayInfo("Nothing to redo.");
            return;
        }

        try {
            UndoableAction operation = undoRedoManager.redo();
            if (operation != null) {
                ui.displaySuccess("Operation redone successfully!");
            } else {
                ui.displayError("Failed to redo operation.");
            }
        } catch (Exception e) {
            ui.displayError("Error during redo: " + e.getMessage());
        }
    }

    public void displayUndoRedoStatus() {
        boolean canUndo = undoRedoManager.canUndo();
        boolean canRedo = undoRedoManager.canRedo();

        //ui.displayInfo("Undo/Redo Status:");
        ui.displayInfo("  Undo available: " + (canUndo ? "Yes" : "No"));
        ui.displayInfo("  Redo available: " + (canRedo ? "Yes" : "No"));

        if (canUndo) {
            UndoableAction lastOp = undoRedoManager.getLastOperation();
            String action = lastOp.getAction().toString().replace("_", " ");
            ui.displayInfo("  Last operation: " + action + " on " + lastOp.getEntityType());
        }
    }
}