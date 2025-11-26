package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.io.File;

/**
 * Deletes a file from the current directory.
 * Simplified to work with just filename instead of full path
 * Usage: "delete file myfile.txt" or "remove file oldnotes.txt"
 */
public class DeleteFileCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify a filename. Example: delete file myfile.txt";
        }

        String filename = arg.trim();
        File file = new File(filename);

        if (!file.exists()) {
            return "File not found: " + filename;
        }

        if (file.isDirectory()) {
            return "This command deletes files only. This is a directory: " + filename;
        }

        try {
            if (file.delete()) {
                return "File deleted successfully: " + file.getName();
            } else {
                return "Failed to delete file (permission denied): " + filename;
            }
        } catch (Exception e) {
            return "Error deleting file: " + e.getMessage();
        }
    }
}
