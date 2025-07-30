package org.example.menu.view;

public class MainView {
    private static MainView mainView;

    public static MainView getInstance() {
        if (mainView == null) {
            mainView = new MainView();
        }
        return mainView;
    }

    public void printMenuOptions() {
        System.out.println("""
                
                === Meniu Principal ===
                1. Postări
                2. Comentarii
                3. Utilizatori
                4. Voturi
                5. Exit
                Alege o opțiune (1-5):
                """);
    }
}
