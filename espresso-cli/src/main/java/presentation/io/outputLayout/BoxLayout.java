package presentation.io.outputLayout;

public final class BoxLayout {
    public final char topLeft;
    public final char topRight;
    public final char bottomLeft;
    public final char bottomRight;
    public final char horizontal;
    public final char vertical;

    private BoxLayout(char topLeft, char topRight, char bottomLeft, char bottomRight, char horizontal, char vertical) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    //varianta cu round corners
    public static BoxLayout ROUNDED() {
        return new BoxLayout('╭', '╮', '╰', '╯', '─', '│');
    }
    //daca e timp: varianta cu muchii ascutite
}
