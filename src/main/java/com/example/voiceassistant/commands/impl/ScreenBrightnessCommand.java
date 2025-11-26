package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import com.example.voiceassistant.utils.NumberParser;

/**
 * Controls screen brightness.
 * Usage: "brightness 75" or "brightness seventy five"
 */
public class ScreenBrightnessCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify brightness level (0-100). Example: brightness 75";
        }

        int brightness = NumberParser.parseNumber(arg.trim());
        
        if (brightness < 0 || brightness > 100) {
            return "Brightness must be between 0 and 100. You said: " + arg;
        }

        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "powershell", "-Command",
                        "Get-WmiObject -Namespace root\\wmi -Class WmiMonitorBrightnessMethods | " +
                        "ForEach-Object { $_.WmiSetBrightness(1, " + brightness + ") }"});
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"osascript", "-e",
                        "tell application \"System Events\" to set brightness of (displays) to " + brightness});
            } else {
                Runtime.getRuntime().exec(new String[]{"xrandr", "--output", "HDMI-1", "--brightness",
                        String.format("%.2f", brightness / 100.0)});
            }

            return "Brightness set to " + brightness + "%.";
        } catch (Exception e) {
            return "Failed to set brightness: " + e.getMessage();
        }
    }
}
