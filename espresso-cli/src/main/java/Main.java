import com.fasterxml.jackson.databind.ObjectMapper;
import config.Wiring;
import presentation.EspressoCLI;
import presentation.io.ConsoleIO;
import presentation.io.Renderer;
import undo.UndoRedoManager;
import undo.revert.CommentUpdateReverter;
import undo.revert.PostUpdateReverter;
import undo.utils.UndoRedoUtils;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Wiring wiring = new Wiring();

        ObjectMapper objectMapper = wiring.getObjectMapper();
        PostUpdateReverter postReverter = new PostUpdateReverter(wiring.getPostService(), objectMapper);
        CommentUpdateReverter commentReverter = new CommentUpdateReverter(wiring.getCommentService(), objectMapper);

        UndoRedoManager.initialize(
                Arrays.asList(postReverter, commentReverter),
                UndoRedoUtils.getMaxOperations()
        );

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