package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.io.File;

/**
 * Lists files in the current directory.
 * Simplified to work in current directory by default, no path argument needed
 * Usage: "show files" or "list files"
 */
public class ListFilesCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        // Always use current directory - no path arguments needed
        String dirPath = ".";
        File dir = new File(dirPath);

        if (!dir.exists()) {
            return "Directory not found: " + dirPath;
        }

        if (!dir.isDirectory()) {
            return "Path is not a directory: " + dirPath;
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return "Directory is empty.";
        }

        StringBuilder result = new StringBuilder("Files in current directory:\n");
        for (File file : files) {
            String type = file.isDirectory() ? "[FOLDER]" : "[FILE]";
            result.append(type).append(" ").append(file.getName()).append("\n");
        }

        return result.toString();
    }
}
