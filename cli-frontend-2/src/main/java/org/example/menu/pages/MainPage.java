package org.example.menu.pages;

import org.example.menu.MenuPage;
import java.util.Scanner;

public class MainPage {

    public MenuPage switchPage(int input) {
        return switch (input) {
            case 1 -> MenuPage.POST_MENU;
            case 2 -> MenuPage.COMMENT_MENU;
            case 3 -> MenuPage.USER_MENU;
            case 4 -> MenuPage.VOTE_MENU;
            default -> null;
        };
    }
}
