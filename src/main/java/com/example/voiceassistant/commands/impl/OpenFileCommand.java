package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Opens a file in the current directory.
 * Simplified to work with just filename instead of full path
 * Usage: "open file document.txt" or "show file myfile.pdf"
 */
public class OpenFileCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify a filename. Example: open file document.txt";
        }

        String filename = arg.trim();
        File file = new File(filename);

        if (!file.exists()) {
            return "File not found: " + filename;
        }

        if (file.isDirectory()) {
            return "Path is a directory, not a file: " + filename;
        }

        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", filename});
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", filename});
            } else {
                Runtime.getRuntime().exec(new String[]{"xdg-open", filename});
            }
            return "Opening file: " + file.getName();
        } catch (Exception e) {
            return "Failed to open file: " + e.getMessage();
        }
    }
}
