package com.example.voiceassistant.config;

import com.example.voiceassistant.exceptions.SetupException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Loads configuration from application.properties and system props.
 * - model.path: where your Vosk model folder resides
 * - notes.file: where to store notes.txt
 */
public class Config {
    private final Properties props = new Properties();

    public Config() throws SetupException {
        System.out.println("[Config] Initializing configuration...");

        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) {
                props.load(in);
                System.out.println("[Config] Loaded application.properties successfully!");
            } else {
                System.err.println("[Config] WARNING: application.properties not found on classpath!");
            }
        } catch (IOException e) {
            throw new SetupException("Failed to load application.properties", e);
        }

        // Debug print to verify what we loaded
        System.out.println("[Config] Loaded properties: " + props);
    }

    /**
     * Returns the model path from either system property or application.properties.
     * Performs checks to ensure the path actually exists and is a directory.
     */
    public String getModelPath() throws SetupException {
        String path = System.getProperty("model.path", props.getProperty("model.path"));
        if (path == null || path.isBlank()) {
            throw new SetupException(
                    "model.path is not configured. Set it in application.properties or via -Dmodel.path=/path/to/model"
            );
        }

        System.out.println("[Config] Model path set to: " + path);

        // Verify that the folder actually exists
        File modelDir = new File(path);
        if (!modelDir.exists()) {
            throw new SetupException("[Config] Model directory does not exist: " + modelDir.getAbsolutePath());
        }
        if (!modelDir.isDirectory()) {
            throw new SetupException("[Config] Model path is not a directory: " + modelDir.getAbsolutePath());
        }

        System.out.println("[Config] Verified model directory exists and is accessible.");

        return path;
    }

    /**
     * Returns the path to the notes file.
     */
    public Path getNotesFile() {
        String p = System.getProperty("notes.file", props.getProperty("notes.file", "notes.txt"));
        System.out.println("[Config] Notes file path: " + p);
        return Path.of(p);
    }
}
