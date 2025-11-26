package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;

/**
 * Shuts down the computer.
 * Usage: "shutdown" or "shutdown in 60" (seconds)
 */
public class ShutdownCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            int delay = 0;

            if (arg != null && !arg.trim().isEmpty()) {
                try {
                    delay = Integer.parseInt(arg.trim());
                } catch (NumberFormatException e) {
                    return "Invalid delay. Please specify seconds as a number.";
                }
            }

            if (os.contains("win")) {
                if (delay > 0) {
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "shutdown", "/s", "/t", String.valueOf(delay)});
                } else {
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "shutdown", "/s", "/t", "30"});
                }
            } else {
                if (delay > 0) {
                    Runtime.getRuntime().exec(new String[]{"sudo", "shutdown", "-h", "+" + (delay / 60)});
                } else {
                    Runtime.getRuntime().exec(new String[]{"sudo", "shutdown", "-h", "+1"});
                }
            }

            return "Shutdown initiated. System will shut down in " + (delay > 0 ? delay : 30) + " seconds.";
        } catch (Exception e) {
            return "Failed to initiate shutdown: " + e.getMessage();
        }
    }
}
