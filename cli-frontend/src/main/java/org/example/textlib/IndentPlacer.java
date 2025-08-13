package org.example.textlib;

public class IndentPlacer {
    private final static String INDENT = "  ";

    public static String place(int indentLevel) {
        return INDENT.repeat(indentLevel);
    }
}
