package com.slimtrade.core.utility;

public class StringUtil {

    public static String enumToString(String input) {
        input = input.replaceAll("_", " ");
        input = input.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (i == 0 || input.charAt(i - 1) == ' ') {
                builder.append(Character.toUpperCase(input.charAt(i)));
            } else {
                builder.append(input.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String cleanString(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }

    public static boolean isEmptyString(String input) {
        return input.matches("\\s*");
    }

    public static String cleanPath(String path) {
        return path.replaceAll("\\\\", "/");
    }
}

