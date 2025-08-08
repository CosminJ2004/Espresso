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

        System.out.println("Bine ai venit pe reddit espresso - Sa ai spor la cafeluta suflet drag");
        System.out.println("""
                 ((     ___
                  ))  \\___/_  Bottomless
                 |~~| /~~~\\ \\ cup o'
                C|__| \\___/   coffee
                """);

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
