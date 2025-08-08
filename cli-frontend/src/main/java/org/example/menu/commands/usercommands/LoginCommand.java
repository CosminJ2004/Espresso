package org.example.menu.commands.usercommands;

import org.example.menu.commands.IMenuCommand;
import org.example.menu.views.View;
import org.example.menu.views.ViewID;
import org.example.menu.views.ViewManager;

public class LoginCommand implements IMenuCommand {
    public boolean execute(View view) {
        ViewManager viewManager = view.getViewManager();
        try {
            viewManager.getUserService().loginUser(viewManager.getApiUserService());
        } catch (Exception e) {
            System.err.println("Error while logging in: " + e.getMessage());
        }
        viewManager.switchToNextView(ViewID.MAIN_MENU);
        return true;
    }
}
