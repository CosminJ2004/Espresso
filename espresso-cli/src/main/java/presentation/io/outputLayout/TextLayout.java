package presentation.io.outputLayout;

import java.util.ArrayList;
import java.util.List;

public final class TextLayout {
    private TextLayout() {
    }

    //codurile ansi pentru culori ocupa 2 caractere, deci trebuie sa le eliminam cand luam in calcul lungimea textului
    public static String stripAnsiColor(String s) {
        return s.replaceAll("\\u001B\\[[;\\d]*m", "");
    }

    public static List<String> wrapWords(String text, int maxLen) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();

        for (String w : words) {
            if (w.length() > maxLen) {
                if (line.length() > 0) {
                    lines.add(line.toString());
                    line.setLength(0);
                }
                int i = 0;
                while (i < w.length()) {
                    int end = Math.min(i + maxLen, w.length());
                    lines.add(w.substring(i, end));
                    i = end;
                }
            } else {
                if (line.length() == 0) {
                    line.append(w);
                } else if (line.length() + 1 + w.length() <= maxLen) {
                    line.append(' ').append(w);
                } else {
                    lines.add(line.toString());
                    line.setLength(0);
                    line.append(w);
                }
            }
        }
        if (line.length() > 0) lines.add(line.toString());
        return lines;
    }

    public static String center(String s, int width) {
        int len = stripAnsiColor(s).length();
        if (len >= width) return s;
        int left = (width - len) / 2;
        int right = width - len - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }

    public static String padRight(String s, int width) {
        int len = stripAnsiColor(s).length();
        if (len >= width) return s;
        return s + " ".repeat(width - len);
    }
}
