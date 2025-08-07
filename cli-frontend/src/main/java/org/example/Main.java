package org.example;

import org.example.menu.MenuOption;
import org.example.menu.views.View;
import org.example.menu.views.ViewID;
import org.example.menu.views.ViewManager;

import java.util.Scanner;

import static org.example.textlib.InputTranslator.translateInput;

public class Main {

    public static void main(String[] args) {
        ViewManager viewManager = ViewManager.getInstance();

        System.out.println("CLI Java Client - Interfață interactivă");

        Scanner sc  = new Scanner(System.in);
        boolean isActive = true;

        while(isActive) {
            View currentView = viewManager.getCurrentViewObject();
            ViewID currentViewID = viewManager.getCurrentViewID();

            currentView.displayMenu();

            String input = sc.nextLine();
            MenuOption menuOption = translateInput(input, currentViewID);

            isActive = currentView.activateMenuOption(menuOption);
        }
    }

}
