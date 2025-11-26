package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Searches the web using default browser.
 * Usage: "search java programming" or "google machine learning"
 */
public class SearchWebCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify search terms. Example: search java programming";
        }

        String query = arg.trim();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String searchUrl = "https://www.google.com/search?q=" + encodedQuery;

        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", searchUrl});
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", searchUrl});
            } else {
                Runtime.getRuntime().exec(new String[]{"xdg-open", searchUrl});
            }

            return "Searching for: " + query;
        } catch (Exception e) {
            return "Failed to search: " + e.getMessage();
        }
    }
}
