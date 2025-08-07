package org.example.menu.views;

import org.example.menu.MenuOption;
import org.example.menu.commands.IMenuCommand;
import org.example.menu.commands.QuitCommand;
import org.example.menu.commands.maincommands.OpenPostMenuCommand;
import org.example.menu.commands.maincommands.OpenUserMenuCommand;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ViewSetup {

    protected static View initMainMenu() {
        View mainMenu =  new View();

        mainMenu.setViewID(ViewID.MAIN_MENU);
        //TODO inlocuieste cu TreeMap
        LinkedHashMap<MenuOption, IMenuCommand> menu = new LinkedHashMap<>();
        menu.put(MenuOption.OPEN_POST_MENU, new OpenPostMenuCommand());
        menu.put(MenuOption.OPEN_USER_MENU, new OpenUserMenuCommand());
        menu.put(MenuOption.QUIT, new QuitCommand());

        mainMenu.setMenu(menu);

        return mainMenu;
    }

    protected static View initPotMenu() {
        View postMenu = new View();

        postMenu.setViewID(ViewID.POST_MENU);

        HashMap<MenuOption, IMenuCommand> menu = new HashMap<>(Map.of(

        ));

        return postMenu;
    }

    protected static void linkViews(HashMap<ViewID, View> views) {

    }

    protected static void linkMainMenu(HashMap<ViewID, View> views) {
        HashMap<ViewID, View> nextViewsMainMenu = new HashMap<>();
        nextViewsMainMenu.put(ViewID.POST_MENU, views.get(ViewID.POST_MENU));
        nextViewsMainMenu.put(ViewID.USER_MENU, views.get(ViewID.USER_MENU));

        views.get(ViewID.MAIN_MENU).setNextViews(nextViewsMainMenu);
    }
}
