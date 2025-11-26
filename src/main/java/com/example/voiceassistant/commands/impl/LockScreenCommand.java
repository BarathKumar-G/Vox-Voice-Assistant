package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;

/**
 * Locks the screen.
 * Usage: "lock screen"
 */
public class LockScreenCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"rundll32.exe", "user32.dll,LockWorkStation"});
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"/System/Library/CoreServices/Menu Extras/User.menu/Contents/Resources/CGSession", "-suspend"});
            } else {
                Runtime.getRuntime().exec(new String[]{"gnome-screensaver-command", "-l"});
            }

            return "Screen locked successfully.";
        } catch (Exception e) {
            return "Failed to lock screen: " + e.getMessage();
        }
    }
}
