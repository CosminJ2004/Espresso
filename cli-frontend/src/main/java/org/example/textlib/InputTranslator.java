package org.example.textlib;

import org.example.menu.MenuOption;
import org.example.menu.views.ViewID;

public class InputTranslator {
    public static MenuOption translateInput(String input, ViewID viewID) {
        String stringInput = translateInputToString(input, viewID);

        return switch (stringInput) {
            case "quit" -> MenuOption.QUIT;

            default -> null;
        };
    }

    private static String translateInputToString(String input, ViewID viewID) {
        switch (viewID) {
            case MAIN_MENU:
                return translateMenuInput(input);
        }

        return input;
    }

    private static String translateMenuInput(String input) {
        // 1. Post menu, 2. User menu, 3. Quit
        return switch (input) {
            case "1" -> "post menu";
            case "2" -> "user menu";
            case "3" -> "quit";
            default -> input;
        };
    }
}
