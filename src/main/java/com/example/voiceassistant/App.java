package com.example.voiceassistant;

import com.example.voiceassistant.core.AssistantController;
import com.example.voiceassistant.ui.MainWindow;

import javax.swing.SwingUtilities;

/**
 * Application entry point. Creates UI and wires controller.
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            AssistantController controller = new AssistantController(window);
            window.setController(controller);
            window.setVisible(true);
        });
    }
}
