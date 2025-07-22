package com.example.demo.utils;

import com.example.demo.logger.*;
import java.util.Scanner;

public class Console {
    private static final Console instance = new Console();
    private static ProfanityFilter filter;
    private static Scanner scanner;

    private static final String INVALID_INPUT_INT = "The format is invalid, please retry.";
    private static final String INVALID_INPUT_TEXT = "The text cannot be empty.";
    private static final String INVALID_INPUT_CRED = "Credentials doesn't have the required minimum of characters, nor they contain profanity";
    private static final int MINIM_CRED_CHARS = 5;

    private Console() {
        filter = new ProfanityFilter("BadWords.txt");
        scanner = new Scanner(System.in);
    }

    public static Console getInstance() {
        return instance;
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static int readInt(String prompt) {
        Console.println(prompt);
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                Log.log(LogLevel.DEBUG, "User Input:" + input);
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println(INVALID_INPUT_INT);
            }
        }
    }

    public static String readText(String prompt) {
        Console.println(prompt);
        while (true) {
            String input = scanner.nextLine().trim();
            Log.log(LogLevel.DEBUG, "User Input: " + input);
            if (input.isEmpty()) {
                System.out.println(INVALID_INPUT_TEXT);
            } else {
                return filter.censorText(input);
            }
        }
    }

    public static String readCredential(String prompt) {
        Console.println(prompt);
        while (true) {
            String input = scanner.nextLine().trim();
            Log.log(LogLevel.DEBUG, "User Input: " + input);
            if (input.length() < MINIM_CRED_CHARS && filter.containsProfanity(input)) {
                Console.println(INVALID_INPUT_CRED);
            } else {
                return input;
            }
        }
    }
}
