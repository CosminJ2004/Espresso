package org.example.menu;

public enum MenuOption {
    OPEN_POST_MENU,
    OPEN_COMMENT_MENU,
    OPEN_VOTE_MENU,
    OPEN_USER_MENU,
    QUIT,
    BACK;

    @Override
    public String toString() {
        String lowercase = name().toLowerCase();
        String spaced = lowercase.replace('_', ' ');

        return spaced.substring(0, 1).toUpperCase() + spaced.substring(1);
    }
}
