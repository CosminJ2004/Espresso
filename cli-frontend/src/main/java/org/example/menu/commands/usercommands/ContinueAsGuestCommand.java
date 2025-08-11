package org.example.menu.commands.usercommands;

import org.example.menu.commands.IMenuCommand;
import org.example.menu.views.View;
import org.example.menu.views.ViewID;
import org.example.menu.views.ViewManager;

public class ContinueAsGuestCommand implements IMenuCommand {
    public boolean execute(View view) {
        ViewManager viewManager = view.getViewManager();
        viewManager.getUserService().continueAsGuest();
        viewManager.switchToNextView(ViewID.MAIN_MENU);
        return true;
    }
}
