package org.example.ui;

import org.example.models.input.LoginInput;
import org.example.models.input.RegisterInput;

import java.util.Scanner;

public class UserUI {
    private static UserUI instance;

    private UserUI() {
    }

    public static UserUI getInstance() {
        if (instance == null) {
            instance = new UserUI();
        }
        return instance;
    }

    public LoginInput getUserDetailsFromLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        return new LoginInput(username, password);
    }

    public RegisterInput getUserDetailsFromRegister() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        return new RegisterInput(username, password);
    }
}
