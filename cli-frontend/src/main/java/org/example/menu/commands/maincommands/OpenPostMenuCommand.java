package org.example.menu.commands.maincommands;

import org.example.menu.commands.IMenuCommand;
import org.example.menu.views.View;
import org.example.menu.views.ViewID;

public class OpenPostMenuCommand implements IMenuCommand {

    public boolean execute(View view) {
        view.getViewManager().switchToNextView(ViewID.POST_MENU);
        return true;
    }
}
