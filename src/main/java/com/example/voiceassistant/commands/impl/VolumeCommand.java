package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import com.example.voiceassistant.utils.NumberParser;
import java.io.IOException;

/**
 * Controls system volume.
 * Usage: "volume 50", "volume fifty", "mute", or "unmute"
 */
public class VolumeCommand implements Command {

    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify volume level (0–100), 'mute', or 'unmute'.";
        }

        String command = arg.trim().toLowerCase();

        try {
            if (command.equals("mute")) {
                muteSystemVolume();
                return "Volume muted.";
            } else if (command.equals("unmute")) {
                unmuteSystemVolume();
                return "Volume unmuted.";
            } else {
                int volume = NumberParser.parseNumber(command);
                if (volume < 0 || volume > 100) {
                    return "Volume must be between 0 and 100. You said: " + command;
                }
                setSystemVolume(volume);
                return "Volume set to " + volume + "%.";
            }
        } catch (IOException e) {
            return "Failed to control volume: " + e.getMessage();
        }
    }

    private void setSystemVolume(int volume) throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            int volumeValue = (volume * 65535) / 100;

            // ✅ Adjust this path to where you place nircmd.exe
            String nircmdPath = "C:\\Tools\\nircmd.exe";

            if (new java.io.File(nircmdPath).exists()) {
                new ProcessBuilder(nircmdPath, "setsysvolume", String.valueOf(volumeValue))
                        .inheritIO().start().waitFor();
            } else {
                throw new IOException("NirCmd not found at " + nircmdPath +
                        ". Download it from https://www.nirsoft.net/utils/nircmd.html");
            }

        } else if (os.contains("mac")) {
            new ProcessBuilder("osascript", "-e",
                    "set volume output volume " + volume).start();

        } else {
            // Linux: requires amixer (usually part of ALSA)
            new ProcessBuilder("amixer", "set", "Master", volume + "%").start();
        }
    }

    private void muteSystemVolume() throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            String nircmdPath = "C:\\Tools\\nircmd.exe";
            if (new java.io.File(nircmdPath).exists()) {
                new ProcessBuilder(nircmdPath, "mutesysvolume", "1")
                        .inheritIO().start().waitFor();
            } else {
                throw new IOException("NirCmd not found at " + nircmdPath);
            }

        } else if (os.contains("mac")) {
            new ProcessBuilder("osascript", "-e", "set volume output muted true").start();

        } else {
            new ProcessBuilder("amixer", "set", "Master", "mute").start();
        }
    }

    private void unmuteSystemVolume() throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            String nircmdPath = "C:\\Tools\\nircmd.exe";
            if (new java.io.File(nircmdPath).exists()) {
                new ProcessBuilder(nircmdPath, "mutesysvolume", "0")
                        .inheritIO().start().waitFor();
                // Optionally set a default volume
                int defaultVolume = (50 * 65535) / 100;
                new ProcessBuilder(nircmdPath, "setsysvolume", String.valueOf(defaultVolume))
                        .inheritIO().start().waitFor();
            } else {
                throw new IOException("NirCmd not found at " + nircmdPath);
            }

        } else if (os.contains("mac")) {
            new ProcessBuilder("osascript", "-e", "set volume output muted false").start();

        } else {
            new ProcessBuilder("amixer", "set", "Master", "unmute").start();
        }
    }
}
