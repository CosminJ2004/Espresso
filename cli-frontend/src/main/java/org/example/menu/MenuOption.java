package org.example.menu;

public enum MenuOption {
    LOGIN_MENU,
    LOGIN,
    REGISTER,
    CONTINUE_AS_GUEST,
    OPEN_POST_MENU,
    SHOW_FEED,
    CREATE_POST,
    UPDATE_POST,
    DELETE_POST,
    ADD_COMMENT,
    OPEN_USER_MENU,
    OPEN_COMMENT_MENU,
    OPEN_VOTE_MENU,
    QUIT,
    BACK;

    @Override
    public String toString() {
        String lowercase = name().toLowerCase();
        String spaced = lowercase.replace('_', ' ');

        return spaced.substring(0, 1).toUpperCase() + spaced.substring(1);
    }
}
