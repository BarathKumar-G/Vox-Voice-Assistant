package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Copies text to clipboard.
 * Usage: "copy hello world" or "clipboard this is my text"
 */
public class ClipboardCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify text to copy. Example: copy hello world";
        }

        try {
            String text = arg.trim();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(text);
            clipboard.setContents(selection, null);

            return "Copied to clipboard: " + text;
        } catch (Exception e) {
            return "Failed to copy to clipboard: " + e.getMessage();
        }
    }
}
