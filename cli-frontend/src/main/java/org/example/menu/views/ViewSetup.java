package org.example.menu.views;

import org.example.menu.MenuOption;
import org.example.menu.commands.IMenuCommand;
import org.example.menu.commands.QuitCommand;
import org.example.menu.commands.maincommands.*;
import org.example.menu.commands.postcommands.*;
import org.example.menu.commands.usercommands.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ViewSetup {

    protected static View initMainMenu() {
        View mainMenu =  new View();


        mainMenu.setViewID(ViewID.MAIN_MENU);

        LinkedHashMap<MenuOption, IMenuCommand> menu = new LinkedHashMap<>();
        menu.put(MenuOption.OPEN_POST_MENU, new OpenPostMenuCommand());
        menu.put(MenuOption.OPEN_USER_MENU, new OpenUserMenuCommand());
        menu.put(MenuOption.QUIT, new QuitCommand());

        mainMenu.setMenu(menu);

        return mainMenu;
    }

    protected static View initLoginMenu() {
        View loginMenu = new View();

        loginMenu.setViewID(ViewID.LOGIN_MENU);

        LinkedHashMap<MenuOption, IMenuCommand> menu = new LinkedHashMap<>();
        menu.put(MenuOption.LOGIN, new LoginCommand());
//        menu.put(MenuOption.REGISTER, new RegisterCommand());
//        menu.put(MenuOption.CONTINUE_AS_GUEST, new ContinueAsGuestCommand());

        loginMenu.setMenu(menu);

        return loginMenu;
    }

    protected static View initPostMenu() {
        View postMenu = new View();

        postMenu.setViewID(ViewID.POST_MENU);

        LinkedHashMap<MenuOption, IMenuCommand> menu = new LinkedHashMap<>();
        menu.put(MenuOption.SHOW_FEED, new ShowFeedCommand());
        menu.put(MenuOption.CREATE_POST, new CreatePostCommand());
        menu.put(MenuOption.ADD_COMMENT, null);
        menu.put(MenuOption.EDIT_POST, new EditPostCommand());
        menu.put(MenuOption.DELETE_POST, new DeletePostCommand());
        menu.put(MenuOption.QUIT, new QuitCommand());

        postMenu.setMenu(menu);

        return postMenu;
    }

    protected static void linkViews(HashMap<ViewID, View> views) {
        linkMainMenu(views);
        linkPostMenu(views);
    }

    protected static void linkMainMenu(HashMap<ViewID, View> views) {
        HashMap<ViewID, View> nextViewsMainMenu = new HashMap<>();
        nextViewsMainMenu.put(ViewID.POST_MENU, views.get(ViewID.POST_MENU));
        nextViewsMainMenu.put(ViewID.USER_MENU, views.get(ViewID.USER_MENU));

        views.get(ViewID.MAIN_MENU).setNextViews(nextViewsMainMenu);
    }

    protected static void linkPostMenu(HashMap<ViewID, View> views) {
        HashMap<ViewID, View> nextViewsPostMenu = new HashMap<>();
        nextViewsPostMenu.put(ViewID.COMMENT_MENU, views.get(ViewID.COMMENT_MENU));
        nextViewsPostMenu.put(ViewID.VOTE_MENU, views.get(ViewID.VOTE_MENU));

        views.get(ViewID.POST_MENU).setNextViews(nextViewsPostMenu);
    }
}
