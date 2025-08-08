package org.example.textlib;

import org.example.menu.MenuOption;
import org.example.menu.views.ViewID;

public class InputTranslator {
    public static MenuOption translateInput(String input, ViewID viewID) {
        String stringInput = translateInputToString(input, viewID);

        return switch (stringInput) {
            case "post menu" -> MenuOption.OPEN_POST_MENU;
            case "user menu" -> MenuOption.OPEN_USER_MENU;
            case "show feed" -> MenuOption.SHOW_FEED;
            case "create post" -> MenuOption.CREATE_POST;
            case "quit" -> MenuOption.QUIT;

            default -> null;
        };
    }

    private static String translateInputToString(String input, ViewID viewID) {
        switch (viewID) {
            case MAIN_MENU:
                return translateMenuInput(input);

            case POST_MENU:
                return translatePostMenuInput(input);
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

    private static String translatePostMenuInput(String input) {
        //1. Show feed, 2. Create post, 3. Add comment to post, 4 Update post, 5. Delete post
        return switch (input) {
            case "1" -> "show feed";
            case "2" -> "create post";
            default -> input;
        };
    }
}
