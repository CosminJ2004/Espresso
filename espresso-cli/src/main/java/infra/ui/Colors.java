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

    public static String toYellow(String s) {
        return "\u001B[33m" + s + "\u001B[0m";
    }

    public static String toOrange(String s) { // mesaje welcome, goodbye
        return "\u001B[38;5;208m" + s + "\u001B[0m";
    }

    public static String toCyan(String s) {
        return "\u001B[36m" + s + "\u001B[0m";
    }

    public static String toLink(String s) {
        return "\u001B[1;4;36m" + s + "\u001B[0m";
    }

    public static String toMagenta(String s){
        return "\u001B[35m" + s + "\u001B[0m";
    }

    public static String toBrightWhite(String s) {
        return "\u001B[97m" + s + "\u001B[0m";
    }
}
