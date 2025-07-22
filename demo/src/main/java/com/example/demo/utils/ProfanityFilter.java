package com.example.demo.utils;

import com.example.demo.logger.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ProfanityFilter {
    private Set<String> badWords = new HashSet<>();

    public ProfanityFilter(String filePath) {
        loadBadWords(filePath);
    }

    private void loadBadWords(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                badWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            Log.log(LogLevel.ERROR, "Unable to open file." + e.getMessage());
        }
    }

    public boolean containsProfanity(String text) {
        String[] words = text.toLowerCase().split("\\W+");
        for (String word : words) {
            for (String badWord : badWords) {
                if (word.contains(badWord)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String censorText(String text) {
        StringBuilder result = new StringBuilder();
        String[] parts = text.split("(?<=\\b)|(?=\\b)");

        for (String part : parts) {
            String lower = part.toLowerCase();
            boolean censored = false;
            for (String badWord : badWords) {
                if (lower.contains(badWord)) {
                    result.append("*".repeat(part.length()));
                    censored = true;
                    break;
                }
            }
            if (!censored) {
                result.append(part);
            }
        }
        return result.toString();
    }
}
