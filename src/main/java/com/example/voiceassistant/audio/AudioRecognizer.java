package com.example.voiceassistant.audio;

import com.example.voiceassistant.config.Config;
import com.example.voiceassistant.exceptions.AssistantException;
import com.example.voiceassistant.exceptions.AudioException;
import com.google.gson.Gson;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Captures microphone audio, runs Vosk recognition on a background thread and streams results to a listener.
 * Exception handling included, resources closed safely.
 */
public class AudioRecognizer {

    public interface Listener {
        void onStart();
        void onPartial(String text);
        void onFinal(String text);
        void onError(Throwable error);
        void onStop();
    }

    private final Config config;
    private final Listener listener;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "recognition");
        t.setDaemon(true);
        return t;
    });

    private volatile boolean running = false;

    public AudioRecognizer(Config config, Listener listener) {
        this.config = config;
        this.listener = listener;
    }

    public synchronized void start() throws AssistantException {
        if (running) {
            System.out.println("[AudioRecognizer] Already running, start() ignored.");
            return;
        }
        running = true;
        System.out.println("[AudioRecognizer] Starting recognition thread...");
        executor.submit(this::runLoop);
    }

    public synchronized void stop() throws AssistantException {
        System.out.println("[AudioRecognizer] Stop requested.");
        running = false;
        listener.onStop();
    }

    private void runLoop() {
        try {
            String modelPath = config.getModelPath();
            System.out.println("[AudioRecognizer] Model path: " + modelPath);
            java.io.File modelDir = new java.io.File(modelPath);
            System.out.println("[AudioRecognizer] Exists: " + modelDir.exists() + " | IsDir: " + modelDir.isDirectory());

            try (Model model = new Model(modelPath)) {
                System.out.println("[AudioRecognizer] Model loaded successfully!");

                final AudioFormat format = new AudioFormat(16000f, 16, 1, true, false);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                System.out.println("[AudioRecognizer] Checking microphone support...");
                if (!AudioSystem.isLineSupported(info)) {
                    throw new AudioException("Microphone line not supported for format " + format.toString());
                }
                System.out.println("[AudioRecognizer] Microphone format supported: " + format.toString());

                try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
                    line.open(format);
                    line.start();
                    System.out.println("[AudioRecognizer] Microphone opened successfully.");
                    listener.onStart();

                    try (Recognizer recognizer = new Recognizer(model, 16000f)) {
                        System.out.println("[AudioRecognizer] Recognizer initialized.");
                        byte[] buffer = new byte[4096];
                        Gson gson = new Gson();

                        while (running) {
                            int bytesRead = line.read(buffer, 0, buffer.length);
                            if (bytesRead <= 0) continue;

                            boolean isFinal = recognizer.acceptWaveForm(buffer, bytesRead);
                            if (isFinal) {
                                String resultJson = recognizer.getResult();
                                String text = gson.fromJson(resultJson, VoskResult.class).text;
                                if (text != null && !text.isBlank()) {
                                    listener.onFinal(text);
                                }
                            } else {
                                String partialJson = recognizer.getPartialResult();
                                String partial = gson.fromJson(partialJson, VoskPartial.class).partial;
                                if (partial != null) {
                                    listener.onPartial(partial);
                                }
                            }
                        }
                        System.out.println("[AudioRecognizer] Recognition loop exited normally.");
                    }
                }
            }
        } catch (Throwable t) {
            System.err.println("[AudioRecognizer] ERROR: " + t.getMessage());
            t.printStackTrace();
            listener.onError(t);
        } finally {
            running = false;
            System.out.println("[AudioRecognizer] Recognition stopped, cleaning up.");
            listener.onStop();
        }
    }

    // Minimal DTOs for Vosk JSON payloads
    private static class VoskResult {
        String text;
    }

    private static class VoskPartial {
        String partial;
    }
}
