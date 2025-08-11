package org.example.services;

import com.google.gson.Gson;
import org.example.apiservices.ApiUserService;
import org.example.menu.views.ViewManager;
import org.example.models.User;
import org.example.models.input.LoginInput;
import org.example.models.input.RegisterInput;
import org.example.ui.UserUI;
import org.example.utils.ApiResult;

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

    public boolean loginUser(ApiUserService apiUserService) { //true sau false, ca sa stim pt viewManager ce meniu bagam
        LoginInput loginInput = userUI.getUserDetailsFromLogin();
        String username = loginInput.username();
        String password = loginInput.password();
        String json = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, username, password);

        ApiResult<User> result = apiUserService.handleLogin(json, gson);
        if (result.isOk()) {
            System.out.println("Felicitari! Te-ai conectat cu succes!");
            ViewManager.user = result.get();
            return true;
        } else {
            System.out.println("A aparut o eroare: " + result.message());
            ViewManager.user = null;
            return false;
        }
    }

    public boolean registerUser(ApiUserService apiUserService) {
        RegisterInput registerInput = userUI.getUserDetailsFromRegister();
        String username = registerInput.username();
        String password = registerInput.password();
        String json = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, username, password);

        ApiResult<User> result = apiUserService.handleRegister(json, gson);

        if (result.isOk()) {
            System.out.println("Felicitari! Cont creat cu succes!");
            System.out.println("Pentru a continua ca utilizator, trebuie sa intri in contul creat.");
            return true;
        } else {
            System.err.println("Nu s-a putut crea contul: " + result.message());
            ViewManager.user = null;
            return false;
        }
    }

    public void continueAsGuest() {
        ViewManager.user = null;
        System.out.println("Sunteti vizitator, unele functii pot fi limitate pentru utilizatorii neautentificati.");
    }
}
