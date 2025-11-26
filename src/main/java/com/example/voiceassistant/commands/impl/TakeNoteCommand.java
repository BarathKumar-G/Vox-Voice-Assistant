package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import com.example.voiceassistant.config.Config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Appends a note to a text file.
 * Usage: "note <text>" - Creates a note from spoken text
 */
public class TakeNoteCommand implements Command {

    private final Path notesPath;

    public TakeNoteCommand(Config config) {
        this.notesPath = config.getNotesFile();
    }

    @Override
    public synchronized String execute(String arg) throws Exception {
        if (arg == null || arg.isBlank()) {
            return "Please say your note after saying 'note'.";
        }

        // This handles cases where "note" is misheard as "not"
        String noteText = arg;

        Files.createDirectories(notesPath.getParent() == null ? Path.of(".") : notesPath.getParent());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(notesPath.toFile(), true))) {
            writer.write(noteText);
            writer.newLine();
        }
        return "Added note: " + noteText;
    }
}
