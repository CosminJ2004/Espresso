
public class Main {
    public static void main(String[] args) {
        Service service = new Service();
        Menu menu = new Menu(service);

        while (true) {
            menu.createLoginMenu();
            menu.createMainMenu();
        }
    }
}