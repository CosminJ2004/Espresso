package org.example.services;

import com.google.gson.Gson;
import org.example.apiservices.ApiUserService;
import org.example.menu.views.ViewManager;
import org.example.models.User;
import org.example.models.input.LoginInput;
import org.example.ui.UserUI;

public class UserService {
    private static UserService instance;
    private UserUI userUI = UserUI.getInstance();
    private Gson gson;

    private UserService(Gson gson) {
        this.gson = gson;
    }

    public static UserService getInstance(Gson gson) {
        if (instance == null) {
            instance = new UserService(gson);
        }
        return instance;
    }

    public void loginUser(ApiUserService apiUserService) throws Exception {
        LoginInput loginInput = userUI.getUserDetailsFromLogin();
        String username = loginInput.username();
        String password = loginInput.password();
        String json = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, username, password);

        ViewManager.user = gson.fromJson(apiUserService.handleLogin(json), User.class);
        if(ViewManager.user == null){
            System.out.println("Login failed. Please check your credentials.");
        }
    }

}
