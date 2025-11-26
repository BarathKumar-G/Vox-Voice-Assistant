package com.example.voiceassistant.core;

import com.example.voiceassistant.audio.AudioRecognizer;
import com.example.voiceassistant.commands.Command;
import com.example.voiceassistant.commands.CommandRegistry;
import com.example.voiceassistant.commands.impl.*;
import com.example.voiceassistant.config.Config;
import com.example.voiceassistant.exceptions.AssistantException;
import com.example.voiceassistant.exceptions.SetupException;
import com.example.voiceassistant.ui.MainWindow;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Central controller: connects UI, recognizer and commands.
 * Multithreading:
 *  - recognition runs in AudioRecognizer background executor
 *  - commands run in a separate single-thread executor
 */
public class AssistantController {

    private final MainWindow ui;
    private final AudioRecognizer recognizer;
    private final CommandRegistry commands;
    private final ExecutorService commandExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "command-exec");
        t.setDaemon(true);
        return t;
    });

    public AssistantController(MainWindow ui) {
        this.ui = ui;
        try {
            Config config = new Config();
            this.commands = buildCommands(config);
            this.recognizer = new AudioRecognizer(config, new AudioRecognizer.Listener() {
                @Override
                public void onStart() {
                    ui.setStatus("Listening...");
                }

                @Override
                public void onPartial(String text) {
                    ui.setPartialText(text);
                }

                @Override
                public void onFinal(String text) {
                    ui.appendLog("Heard: " + text);
                    parseAndExecute(text);
                }

                @Override
                public void onError(Throwable error) {
                    ui.appendLog("Error: " + error.getMessage());
                    ui.setStatus("Error");
                }

                @Override
                public void onStop() {
                    ui.setStatus("Idle");
                    ui.setPartialText("");
                }
            });

            displayAvailableCommands();
        } catch (SetupException e) {
            throw new RuntimeException("Failed to initialize assistant: " + e.getMessage(), e);
        }
    }

    public void startListening() {
        try {
            recognizer.start();
            ui.setStatus("Starting...");
        } catch (AssistantException e) {
            ui.appendLog("Start error: " + e.getMessage());
            ui.setStatus("Error");
        }
    }

    public void stopListening() {
        try {
            recognizer.stop();
            ui.setStatus("Stopping...");
        } catch (AssistantException e) {
            ui.appendLog("Stop error: " + e.getMessage());
            ui.setStatus("Error");
        }
    }

    private CommandRegistry buildCommands(Config config) {
        CommandRegistry registry = new CommandRegistry();

        // Original commands
        registry.register("open browser", new OpenBrowserCommand());
        registry.register("time", new SystemTimeCommand());
        registry.register("note", new TakeNoteCommand(config));

        // File Management Commands
        registry.register("show files", new ListFilesCommand());
        registry.register("list files", new ListFilesCommand());
        registry.register("open file", new OpenFileCommand());
        registry.register("show file", new OpenFileCommand());
        registry.register("create file", new CreateFileCommand());
        registry.register("delete file", new DeleteFileCommand());
        registry.register("remove file", new DeleteFileCommand());

        // System Control Commands
        registry.register("shutdown", new ShutdownCommand());
        registry.register("restart", new RestartCommand());
        registry.register("lock screen", new LockScreenCommand());
        registry.register("volume", new VolumeCommand());
        registry.register("brightness", new ScreenBrightnessCommand());

        // Media and Application Commands
        registry.register("play", new PlayMediaCommand());
        registry.register("open", new OpenApplicationCommand());
        registry.register("search", new SearchWebCommand());
        registry.register("screenshot", new ScreenshotCommand());
        registry.register("copy", new ClipboardCommand());

        // Utility and Information Commands
        registry.register("system info", new SystemInfoCommand());
        registry.register("calculate", new CalculatorCommand());
        registry.register("ip address", new IPAddressCommand());
        registry.register("help", new HelpCommand());

        return registry;
    }

    private void displayAvailableCommands() {
        try {
            HelpCommand helpCmd = new HelpCommand();
            String helpText = helpCmd.execute("");
            ui.setAvailableCommands(helpText);
        } catch (Exception e) {
            ui.setAvailableCommands("Failed to load commands list.");
        }
    }

    private void parseAndExecute(String rawText) {
        String text = rawText == null ? "" : rawText.trim().toLowerCase(Locale.ROOT);
        if (text.isEmpty()) return;

        Command cmd = null;
        String arg = "";

        // File Management
        if (text.startsWith("show files") || text.startsWith("list files")) {
            cmd = commands.get("show files");
            arg = text.substring(text.startsWith("show files") ? "show files".length() : "list files".length()).trim();
        } else if (text.startsWith("open file ") || text.startsWith("show file ")) {
            cmd = commands.get("open file");
            arg = text.substring(text.startsWith("open file ") ? "open file ".length() : "show file ".length()).trim();
        } else if (text.startsWith("create file ")) {
            cmd = commands.get("create file");
            arg = text.substring("create file ".length()).trim();
        } else if (text.startsWith("delete file ") || text.startsWith("remove file ")) {
            cmd = commands.get("delete file");
            arg = text.substring(text.startsWith("delete file ") ? "delete file ".length() : "remove file ".length()).trim();
        }
        // System Control
        else if (text.startsWith("shutdown")) {
            cmd = commands.get("shutdown");
            arg = text.substring("shutdown".length()).trim();
        } else if (text.startsWith("restart")) {
            cmd = commands.get("restart");
            arg = text.substring("restart".length()).trim();
        } else if (text.contains("lock screen")) {
            cmd = commands.get("lock screen");
        } else if (text.startsWith("volume ")) {
            cmd = commands.get("volume");
            arg = text.substring("volume ".length()).trim();
        } else if (text.startsWith("brightness ")) {
            cmd = commands.get("brightness");
            arg = text.substring("brightness ".length()).trim();
        }
        // Media and Applications
        else if (text.startsWith("play ")) {
            cmd = commands.get("play");
            arg = text.substring("play ".length()).trim();
        } else if (text.startsWith("open ") && !text.startsWith("open file") && !text.startsWith("open browser") && !text.startsWith("show file")) {
            cmd = commands.get("open");
            arg = text.substring("open ".length()).trim();
        } else if (text.startsWith("search ")) {
            cmd = commands.get("search");
            arg = text.substring("search ".length()).trim();
        } else if (text.contains("screenshot")) {
            cmd = commands.get("screenshot");
            arg = text.substring(text.indexOf("screenshot") + "screenshot".length()).trim();
        } else if (text.startsWith("copy ")) {
            cmd = commands.get("copy");
            arg = text.substring("copy ".length()).trim();
        }
        // Utilities
        else if (text.contains("system info")) {
            cmd = commands.get("system info");
        } else if (text.startsWith("calculate ")) {
            cmd = commands.get("calculate");
            arg = text.substring("calculate ".length()).trim();
        } else if (text.contains("ip address") || text.contains("network info")) {
            cmd = commands.get("ip address");
        } else if (text.contains("help")) {
            cmd = commands.get("help");
        }
        // Original commands
        else if (text.contains("open browser")) {
            cmd = commands.get("open browser");
        } else if (text.contains("time")) {
            cmd = commands.get("time");
        } else if (text.startsWith("note ")) {
            cmd = commands.get("note");
            arg = text.substring("note ".length()).trim();
        }

        if (cmd != null) {
            final Command toRun = cmd;
            final String finalArg = arg;
            commandExecutor.submit(() -> {
                try {
                    String result = toRun.execute(finalArg);
                    if (result != null && !result.isBlank()) {
                        ui.appendLog("Assistant: " + result);
                    }
                } catch (Exception e) {
                    ui.appendLog("Command error: " + e.getMessage());
                }
            });
        } else {
            ui.appendLog("No command matched. Say 'help' for available commands.");
        }
    }
}
