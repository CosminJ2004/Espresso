package org.example.menu.commands;

import org.example.menu.views.View;
import org.example.menu.views.ViewID;

public class BackCommand implements IMenuCommand {

    public boolean execute(View view) {
        switch(view.getViewID()) {
            case ViewID.MAIN_MENU:
                return false;

            case ViewID.POST_MENU, ViewID.USER_MENU:
                view.getViewManager().switchToNextView(ViewID.MAIN_MENU);
                break;

            case ViewID.COMMENT_MENU, ViewID.VOTE_MENU:
                view.getViewManager().switchToNextView(ViewID.POST_MENU);
                break;

            case ViewID.REPLY_MENU:
                view.getViewManager().switchToNextView(ViewID.COMMENT_MENU);
        }
        return true;
    }
}
