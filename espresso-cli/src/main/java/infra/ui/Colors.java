package infra.ui;

public final class Colors {
    private Colors() {
    }

    public static String toGreen(String s) {
        return "\u001B[32m" + s + "\u001B[0m";
    }

    public static String toRed(String s) {
        return "\u001B[31m" + s + "\u001B[0m";
    }

    public static String toBlue(String s) {
        return "\u001B[36m" + s + "\u001B[0m";
    }

    public static String toBold(String s) {
        return "\u001B[1m" + s + "\u001B[0m";
    }
}
