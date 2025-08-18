package presentation;

import objects.domain.User;

public final class AppState {
    private static AppState instance;
    private User currentUser;
    private boolean isRunning = true;

    private AppState() {}

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void stop() {
        this.isRunning = false;
    }

    public void reset() {
        this.currentUser = null;
        this.isRunning = true;
    }
}
