package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;

import java.awt.*;
import java.net.URI;

/**
 * Opens the default system browser.
 */
public class OpenBrowserCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI("https://www.google.com"));
            return "Opening browser...";
        }
        return "Desktop integration not supported.";
    }
}
