package org.example.textformatters;

public class FormatIndent {
    private final static String indent = "  ";

    static public String place(int indentLevel) {
        return indent.repeat(indentLevel);
    }
}
