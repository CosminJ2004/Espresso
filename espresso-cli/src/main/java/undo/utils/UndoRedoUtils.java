package undo.utils;

import undo.action.UndoableAction;

public final class UndoRedoUtils { // fisiere dezactivate momentan
    private UndoRedoUtils() {
    }
    private static final int MAX_OPERATIONS = 3;
//    private static final String STORAGE_FILE = "undoRedo.json";
//    private static final String STORAGE_DIR = "currentSession";

    public static int getMaxOperations() {
        return MAX_OPERATIONS;
    }

//    static String getStorageFile() {
//        return STORAGE_FILE;
//    }
//
//    static String getStorageDir() {
//        return STORAGE_DIR;
//    }
}
