package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.io.File;
import java.io.FileWriter;

/**
 * Creates a new file in the current directory.
 * Simplified to work with just filename instead of full path
 * Usage: "create file mynotes.txt" or "create file report with my content"
 */
public class CreateFileCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify a filename. Example: create file myfile.txt";
        }

        String[] parts = arg.trim().split(" with ", 2);
        String filename = parts[0].trim();
        String content = parts.length > 1 ? parts[1].trim() : "";

        File file = new File(filename);

        if (file.exists()) {
            return "File already exists: " + filename;
        }

        try {
            file.createNewFile();

            if (!content.isEmpty()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(content);
                }
            }

            return "File created successfully: " + file.getName();
        } catch (Exception e) {
            return "Failed to create file: " + e.getMessage();
        }
    }
}
