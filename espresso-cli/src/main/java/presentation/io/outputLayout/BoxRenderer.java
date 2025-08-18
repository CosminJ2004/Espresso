package presentation.io.outputLayout;

import infra.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

public final class BoxRenderer {
    private final BoxLayout boxLayout;
    private final int totalWidth;
    private final int innerWidth;

    public BoxRenderer() {
        this.boxLayout = BoxLayout.ROUNDED();
        this.totalWidth = AppConstants.MAX_WIDTH;
        this.innerWidth = totalWidth - 4;
    }

    public BoxRenderer(int innerWidth) {
        this.boxLayout = BoxLayout.ROUNDED();
        this.innerWidth = innerWidth;
        this.totalWidth = innerWidth + 4;
    }

    public List<String> buildBox(String title, List<String> body) {
        List<String> out = new ArrayList<>();

        // top
        out.add(topBorder());

        // title
        if (title != null && !title.isBlank()) {
            String centeredTitle = TextLayout.center(title, innerWidth);
            out.add(contentLine(centeredTitle));
        }

        // body
        if (body != null && !body.isEmpty()) {
            for (String raw : body) {
                String lineText = raw == null ? "" : raw;
                for (String w : TextLayout.wrapWords(lineText, innerWidth)) {
                    out.add(contentLine(TextLayout.padRight(w, innerWidth)));
                }
            }
        }

        // bottom
        out.add(bottomBorder());

        return out;
    }

    private String contentLine(String contentExactInnerWidth) {
        return "" + boxLayout.vertical + " " + contentExactInnerWidth + " " + boxLayout.vertical;
    }

    private String topBorder() {
        return boxLayout.topLeft + repeat(boxLayout.horizontal, totalWidth - 2) + boxLayout.topRight;
    }

    private String bottomBorder() {
        return boxLayout.bottomLeft + repeat(boxLayout.horizontal, totalWidth - 2) + boxLayout.bottomRight;
    }

    private static String repeat(char c, int n) {
        return String.valueOf(c).repeat(Math.max(0, n));
    }
}
