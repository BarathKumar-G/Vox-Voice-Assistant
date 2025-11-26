package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.io.IOException;

/**
 * Opens an application by name.
 * Usage: "open notepad" or "open calculator" or "open chrome"
 */
public class OpenApplicationCommand implements Command {

    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify an application name. Example: open notepad";
        }

        String appName = arg.trim().toLowerCase();
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                openWindowsApp(appName);
            } else if (os.contains("mac")) {
                openMacApp(appName);
            } else {
                openLinuxApp(appName);
            }

            return "Opening " + appName + "...";
        } catch (Exception e) {
            return "Failed to open application: " + e.getMessage();
        }
    }

    private void openWindowsApp(String appName) throws IOException {
        switch (appName) {
            case "notepad":
                new ProcessBuilder("notepad.exe").start();
                break;
            case "calculator":
            case "calc":
                new ProcessBuilder("calc.exe").start();
                break;
            case "paint":
                new ProcessBuilder("mspaint.exe").start();
                break;
            case "word":
                new ProcessBuilder("winword.exe").start();
                break;
            case "excel":
                new ProcessBuilder("excel.exe").start();
                break;
            case "photos":
                new ProcessBuilder("explorer.exe", "shell:appsFolder\\Microsoft.Photos_8wekyb3d8bbwe!App").start();
                break;
            case "terminal":
            case "windows terminal":
                new ProcessBuilder("wt.exe").start();
                break;
            case "powershell":
                new ProcessBuilder("powershell.exe").start();
                break;
            case "cmd":
            case "command prompt":
                new ProcessBuilder("cmd.exe").start();
                break;
            case "chrome":
                new ProcessBuilder("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe").start();
                break;
            case "firefox":
                new ProcessBuilder("C:\\Program Files\\Mozilla Firefox\\firefox.exe").start();
                break;
            case "explorer":
            case "file explorer":
                new ProcessBuilder("explorer.exe").start();
                break;
            default:
                new ProcessBuilder(appName + ".exe").start();
        }
    }

    private void openMacApp(String appName) throws IOException {
        new ProcessBuilder("open", "-a", capitalizeApp(appName)).start();
    }

    private void openLinuxApp(String appName) throws IOException {
        new ProcessBuilder(appName).start();
    }

    private String capitalizeApp(String app) {
        return app.substring(0, 1).toUpperCase() + app.substring(1);
    }
}
