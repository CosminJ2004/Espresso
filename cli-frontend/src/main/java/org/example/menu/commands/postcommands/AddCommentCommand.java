package org.example.menu.commands.postcommands;

import org.example.menu.commands.IMenuCommand;
import org.example.menu.views.View;
import org.example.menu.views.ViewManager;

public class AddCommentCommand implements IMenuCommand {

    public boolean execute(View view) {
        ViewManager viewManager = view.getViewManager();


        return true;
    }
}
