package org.example.menu.views;

import org.example.menu.MenuOption;
import org.example.menu.commands.IMenuCommand;
import org.example.ui.ViewUI;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class View {
    private ViewID viewID;
    private LinkedHashMap<MenuOption, IMenuCommand> menu;
    private HashMap<ViewID, View> nextViews;
    private final ViewManager viewManager = ViewManager.getInstance();
    private ViewUI viewUI = ViewUI.getInstance();

    public View() {}

    public void setViewID(ViewID viewID) {
        this.viewID = viewID;
    }

    public void setMenu(LinkedHashMap<MenuOption, IMenuCommand> menu) {
        this.menu = menu;
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

    public ViewID getViewID() {
        return viewID;
    }

    public void displayMenu() {
        viewUI.renderMenu(menu);
    }

    public boolean activateMenuOption(MenuOption menuOption) {
        return menu.get(menuOption).execute(this);
    }

    public void setNextViews(HashMap<ViewID, View> nextViews) {
        this.nextViews = nextViews;
    }


}
