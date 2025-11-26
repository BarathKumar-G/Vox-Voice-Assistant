package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.io.File;

/**
 * Plays media files (audio/video).
 * Usage: "play /path/to/media.mp3" or "play /path/to/video.mp4"
 */
public class PlayMediaCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify a media file path. Example: play /path/to/song.mp3";
        }

        String filePath = arg.trim();
        File file = new File(filePath);

        if (!file.exists()) {
            return "Media file not found: " + filePath;
        }

        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", filePath});
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", "-a", "QuickTime Player", filePath});
            } else {
                Runtime.getRuntime().exec(new String[]{"vlc", filePath});
            }

            return "Playing: " + file.getName();
        } catch (Exception e) {
            return "Failed to play media: " + e.getMessage();
        }
    }
}
