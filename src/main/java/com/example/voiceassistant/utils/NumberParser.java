package com.example.voiceassistant.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to convert spoken numbers to actual integers.
 * Handles both word numbers ("fifty") and digit numbers ("50").
 */
public class NumberParser {
    private static final Map<String, Integer> WORD_TO_NUMBER = new HashMap<>();

    static {
        // Single digits
        WORD_TO_NUMBER.put("zero", 0);
        WORD_TO_NUMBER.put("one", 1);
        WORD_TO_NUMBER.put("two", 2);
        WORD_TO_NUMBER.put("three", 3);
        WORD_TO_NUMBER.put("four", 4);
        WORD_TO_NUMBER.put("five", 5);
        WORD_TO_NUMBER.put("six", 6);
        WORD_TO_NUMBER.put("seven", 7);
        WORD_TO_NUMBER.put("eight", 8);
        WORD_TO_NUMBER.put("nine", 9);
        WORD_TO_NUMBER.put("ten", 10);

        // Teens
        WORD_TO_NUMBER.put("eleven", 11);
        WORD_TO_NUMBER.put("twelve", 12);
        WORD_TO_NUMBER.put("thirteen", 13);
        WORD_TO_NUMBER.put("fourteen", 14);
        WORD_TO_NUMBER.put("fifteen", 15);
        WORD_TO_NUMBER.put("sixteen", 16);
        WORD_TO_NUMBER.put("seventeen", 17);
        WORD_TO_NUMBER.put("eighteen", 18);
        WORD_TO_NUMBER.put("nineteen", 19);

        // Tens
        WORD_TO_NUMBER.put("twenty", 20);
        WORD_TO_NUMBER.put("thirty", 30);
        WORD_TO_NUMBER.put("forty", 40);
        WORD_TO_NUMBER.put("fifty", 50);
        WORD_TO_NUMBER.put("sixty", 60);
        WORD_TO_NUMBER.put("seventy", 70);
        WORD_TO_NUMBER.put("eighty", 80);
        WORD_TO_NUMBER.put("ninety", 90);

        // Hundreds
        WORD_TO_NUMBER.put("hundred", 100);
    }

    /**
     * Convert text to number (handles "fifty", "50", "5 0" etc.)
     * @param text The text to parse (e.g., "fifty", "50", "five zero")
     * @return The parsed integer value, or -1 if unable to parse
     */
    public static int parseNumber(String text) {
        if (text == null || text.trim().isEmpty()) {
            return -1;
        }

        text = text.trim().toLowerCase();

        // Try direct number parsing first
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            // Continue to word parsing
        }

        // Try parsing as word number
        if (WORD_TO_NUMBER.containsKey(text)) {
            return WORD_TO_NUMBER.get(text);
        }

        // Handle compound words like "twenty five" → 25
        String[] words = text.split("\\s+");
        if (words.length == 2) {
            Integer tens = WORD_TO_NUMBER.get(words[0]);
            Integer ones = WORD_TO_NUMBER.get(words[1]);

            if (tens != null && ones != null && tens >= 20 && tens <= 90 && ones < 10) {
                return tens + ones;
            }
        }

        // Handle digits spoken separately like "5 0" → 50
        StringBuilder digitString = new StringBuilder();
        for (String word : words) {
            Integer digit = WORD_TO_NUMBER.get(word);
            if (digit != null && digit < 10) {
                digitString.append(digit);
            } else {
                return -1; // Invalid format
            }
        }

        if (digitString.length() > 0) {
            try {
                return Integer.parseInt(digitString.toString());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        return -1;
    }
}
