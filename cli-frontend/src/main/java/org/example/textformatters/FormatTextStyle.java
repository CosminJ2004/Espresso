package org.example.textformatters;

public class FormatTextStyle {
    private static FormatTextStyle formatTextStyle;
    private static final String BOLD = "\u001B[1m";
    private static final String RESET = "\u001B[0m";

    public static FormatTextStyle getInstance() {
        if (formatTextStyle == null) {
            formatTextStyle = new FormatTextStyle();
        }
        return formatTextStyle;
    }

    public String applyBold(String text) {
        return BOLD + text + RESET;
    }

}
