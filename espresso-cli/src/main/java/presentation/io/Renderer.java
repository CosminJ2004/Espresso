package presentation.io;


import infra.ui.Colors;
import presentation.io.outputLayout.BoxRenderer;

import java.util.List;

public class Renderer {
    private final BoxRenderer box = new BoxRenderer();

    public void displayWelcomeBanner() {
        List<String> lines = box.buildBox(
                "Espresso CLI",
                List.of("Welcome to Espresso! Easy-to-use & interactive CLI.")
        );
        for (String line : lines) {
            System.out.println(Colors.toBold(Colors.toOrange(line)));
        }
        System.out.println();
    }

//    public void displayGoodbyeBanner() {
//        System.out.println(Colors.toBold(Colors.toOrange("╭─────────────────────────────────────────────────────────╮")));
//        System.out.println(Colors.toBold(Colors.toOrange("│                     Goodbye!                            │")));
//        System.out.println(Colors.toBold(Colors.toOrange("│   Thank you for using Espresso CLI. See you next time!  │")));
//        System.out.println(Colors.toBold(Colors.toOrange("╰─────────────────────────────────────────────────────────╯")));
//        System.out.println();
//    }
//
//    //private ca le folosim intern
//    private void displayLoginMenuBanner() {
//        System.out.println(Colors.toBold(Colors.toYellow("╭─────────────────────────────────────────────────────────╮")));
//        System.out.println(Colors.toBold(Colors.toYellow("│                      SIGN-IN MENU                       │")));
//        System.out.println(Colors.toBold(Colors.toYellow("╰─────────────────────────────────────────────────────────╯")));
//        System.out.println();
//    }
//
//    private void displayUserMenuBanner() {
//        System.out.println(Colors.toBold(Colors.toYellow("╭─────────────────────────────────────────────────────────╮")));
//        System.out.println(Colors.toBold(Colors.toYellow("│                      USER MENU                          │")));
//        System.out.println(Colors.toBold(Colors.toYellow("╰─────────────────────────────────────────────────────────╯")));
//        System.out.println();
//    }
//
//    private void displayPostMenuBanner() {
//        System.out.println(Colors.toBold(Colors.toYellow("╭─────────────────────────────────────────────────────────╮")));
//        System.out.println(Colors.toBold(Colors.toYellow("│                      POSTS MENU                         │")));
//        System.out.println(Colors.toBold(Colors.toYellow("╰─────────────────────────────────────────────────────────╯")));
//        System.out.println();
//    }
//
//    private void displayCommentMenuBanner() {
//        System.out.println(Colors.toBold(Colors.toYellow("╭─────────────────────────────────────────────────────────╮")));
//        System.out.println(Colors.toBold(Colors.toYellow("│                      COMMENTS MENU                      │")));
//        System.out.println(Colors.toBold(Colors.toYellow("╰─────────────────────────────────────────────────────────╯")));
//        System.out.println();
//    }
//
//    public void displayLoginMenu(){
//        displayLoginMenuBanner();
//        System.out.println(Colors.toBold(Colors.toGreen("Please select an option: ")));
//        System.out.println(Colors.toBold(Colors.toGreen("1. Register")));
//        System.out.println(Colors.toBold(Colors.toGreen("2. Login")));
//        System.out.println(Colors.toBold(Colors.toGreen("3. Exit")));
//        System.out.println();
//    }
//
//    public void displayUserMenu() {
//        displayUserMenuBanner();
//        System.out.println(Colors.toBold(Colors.toGreen("Please select an option: ")));
//        System.out.println(Colors.toBold(Colors.toGreen("1. View Profile")));
//        System.out.println(Colors.toBold(Colors.toGreen("2. Edit Profile")));
//        System.out.println(Colors.toBold(Colors.toGreen("3. Search Users")));
//        System.out.println(Colors.toBold(Colors.toGreen("4. Delete Account")));
//        System.out.println(Colors.toBold(Colors.toGreen("5. Logout")));
//        System.out.println();
//    }
//
//    public void displayPostMenu() {
//        displayPostMenuBanner();
//        System.out.println(Colors.toBold(Colors.toGreen("Please select an option: ")));
//        System.out.println(Colors.toBold(Colors.toGreen("1. Create Post")));
//        System.out.println(Colors.toBold(Colors.toGreen("2. View Posts")));
//        System.out.println(Colors.toBold(Colors.toGreen("3. Edit Post")));
//        System.out.println(Colors.toBold(Colors.toGreen("4. Delete Post")));
//        System.out.println(Colors.toBold(Colors.toGreen("5. Vote Post")));
//        System.out.println();
//    }
//
//    public void displayError(String message) {
//        System.out.println(Colors.toBold(Colors.toRed("╭─────────────────────────────────────────────────────────╮")));
//        System.out.println(Colors.toBold(Colors.toRed("│                       [ERROR]                           │")));
//        System.out.println(Colors.toBold(Colors.toRed("│   " + message + "   │")));
//        System.out.println(Colors.toBold(Colors.toRed("╰─────────────────────────────────────────────────────────╯")));
//        System.out.println();
//    }
//
//    public void displaySuccess(String message) {
//        System.out.println(Colors.toBold(Colors.toGreen("╭─────────────────────────────────────────────────────────╮")));
//        System.out.println(Colors.toBold(Colors.toGreen("│                     [SUCCESS]                          │")));
//        System.out.println(Colors.toBold(Colors.toGreen("│   " + message + "   │")));
//        System.out.println(Colors.toBold(Colors.toGreen("╰─────────────────────────────────────────────────────────╯")));
//        System.out.println();
//    }
//
//    public void displayInfo(String message) {
//        System.out.println(Colors.toBold(Colors.toBlue("╭─────────────────────────────────────────────────────────╮")));
//        System.out.println(Colors.toBold(Colors.toBlue("│                      [INFO]                             │")));
//        System.out.println(Colors.toBold(Colors.toBlue("│   " + message + "   │")));
//        System.out.println(Colors.toBold(Colors.toBlue("╰─────────────────────────────────────────────────────────╯")));
//        System.out.println();
//    }
//
//    public void displayInputPrompt(String prompt) {
//        System.out.print(Colors.toBold(Colors.toYellow(prompt)));
//    }
}
