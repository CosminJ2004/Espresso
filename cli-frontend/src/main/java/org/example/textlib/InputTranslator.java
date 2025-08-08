package org.example.textlib;

import org.example.menu.MenuOption;
import org.example.menu.views.ViewID;

public class InputTranslator {
    public static MenuOption translateInput(String input, ViewID viewID) {
        String stringInput = translateInputToString(input, viewID);

        return switch (stringInput) {
            case "login" -> MenuOption.LOGIN;
            case "post menu" -> MenuOption.OPEN_POST_MENU;
            case "user menu" -> MenuOption.OPEN_USER_MENU;
            case "show feed" -> MenuOption.SHOW_FEED;
            case "create post" -> MenuOption.CREATE_POST;
            case "add comment" -> MenuOption.ADD_COMMENT;
            case "edit post" -> MenuOption.EDIT_POST;
            case  "delete post" -> MenuOption.DELETE_POST;
            case "quit" -> MenuOption.QUIT;

            default -> null;
        };
    }

    private static String translateInputToString(String input, ViewID viewID) {
        switch (viewID) {
            case LOGIN_MENU:
                return translateLoginMenuInput(input);

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

    private static String translateLoginMenuInput(String input) {
        // 1. Login, 2. Register, 3. Continue as guest
        return switch (input) {
            case "1" -> "login";
            case "2" -> "register";
            case "3" -> "continue as guest";
            default -> input;
        };
    }

    private static String translatePostMenuInput(String input) {
        //1. Show feed, 2. Create post, 3. Add comment to post, 4 Edit post, 5. Delete post
        return switch (input) {
            case "1" -> "show feed";
            case "2" -> "create post";
            case "3" -> "add comment";
            case "4" -> "edit post";
            case "5" -> "delete post";
            case "6" -> "quit";
            default -> input;
        };
    }
}
