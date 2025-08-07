package org.example.ui;

import org.example.menu.MenuOption;
import org.example.menu.commands.IMenuCommand;

import java.util.HashMap;

public class ViewUI {
    private static ViewUI instance;

    private ViewUI() {}

    public static ViewUI getInstance() {
        if (instance == null) {
            instance = new ViewUI();
        }
        return instance;
    }

    public void renderMenu(HashMap<MenuOption, IMenuCommand> menu) {
        int counter = 1;
        for (MenuOption option : menu.keySet()) {
            System.out.println(counter++ + ". " + option);
        }
    }

}
