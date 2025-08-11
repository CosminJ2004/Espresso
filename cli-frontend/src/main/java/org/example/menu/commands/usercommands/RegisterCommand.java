package org.example.menu.commands.usercommands;

import org.example.menu.commands.IMenuCommand;
import org.example.menu.views.View;
import org.example.menu.views.ViewID;
import org.example.menu.views.ViewManager;

public class RegisterCommand implements IMenuCommand {
    public boolean execute(View view) {
        ViewManager viewManager = view.getViewManager();
        boolean isAccountCreated = viewManager.getUserService().registerUser(viewManager.getApiUserService());
        viewManager.switchToNextView(ViewID.LOGIN_MENU);
        return true;
    }
}