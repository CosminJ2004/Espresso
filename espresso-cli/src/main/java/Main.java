import config.Wiring;
import presentation.EspressoCLI;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;

public class Main {
    public static void main(String[] args) {
        Wiring wiring = new Wiring();

        try(ConsoleIO io = new ConsoleIO()){
            Renderer ui = new Renderer();
            EspressoCLI cli = new EspressoCLI(
                    wiring.getPostService(),
                    wiring.getUserService(),
                    wiring.getCommentService(),
                    io,
                    ui
            );
            cli.run();
        }
    }
}