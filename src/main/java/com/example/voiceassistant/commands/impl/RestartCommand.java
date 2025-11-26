package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;

/**
 * Restarts the computer.
 * Usage: "restart" or "restart in 60" (seconds)
 */
public class RestartCommand implements Command {
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
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "shutdown", "/r", "/t", String.valueOf(delay)});
                } else {
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "shutdown", "/r", "/t", "30"});
                }
            } else {
                if (delay > 0) {
                    Runtime.getRuntime().exec(new String[]{"sudo", "shutdown", "-r", "+" + (delay / 60)});
                } else {
                    Runtime.getRuntime().exec(new String[]{"sudo", "shutdown", "-r", "+1"});
                }
            }

            return "Restart initiated. System will restart in " + (delay > 0 ? delay : 30) + " seconds.";
        } catch (Exception e) {
            return "Failed to initiate restart: " + e.getMessage();
        }
    }
}
