package com.example.voiceassistant.ui;

import com.example.voiceassistant.core.AssistantController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modernized Swing UI with tighter spacing and better balance.
 * Gradient header, compact controls, and soft rounded panels.
 */
public class MainWindow extends JFrame {

    private AssistantController controller;

    private final JButton startButton = new JButton("🎙 Start Listening");
    private final JButton stopButton = new JButton("⏹ Stop Listening");
    private final JLabel statusLabel = new JLabel("Idle");
    private final JLabel partialLabel = new JLabel("...");
    private final JTextArea logArea = new JTextArea(10, 50);
    private final JTextArea commandsArea = new JTextArea(8, 25);

    public MainWindow() {
        super("VoxDesk - Java Voice Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 12));

        // LOG AREA
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(25, 25, 25));
        logArea.setForeground(new Color(0, 255, 140));
        logArea.setMargin(new Insets(10, 10, 10, 10));
        logArea.setBorder(new LineBorder(new Color(70, 70, 70), 1, true));

        // COMMANDS AREA
        commandsArea.setEditable(false);
        commandsArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        commandsArea.setBackground(new Color(250, 250, 250));
        commandsArea.setForeground(new Color(40, 40, 40));
        commandsArea.setMargin(new Insets(10, 10, 10, 10));
        commandsArea.setLineWrap(true);
        commandsArea.setWrapStyleWord(true);
        commandsArea.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));

        partialLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        partialLabel.setForeground(new Color(72, 133, 237));

        // HEADER + CONTROL PANEL combined
        JPanel headerSection = new JPanel(new BorderLayout());
        headerSection.add(createHeaderPanel(), BorderLayout.NORTH);
        headerSection.add(createControlPanel(), BorderLayout.SOUTH);

        // MAIN CONTENT PANELS
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBorder(new EmptyBorder(10, 15, 15, 15));
        contentPanel.setBackground(new Color(250, 250, 250));

        contentPanel.add(createLeftPanel());
        contentPanel.add(createRightPanel());

        // ROOT PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(headerSection, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        wireEvents();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(58, 123, 213),
                        getWidth(), 0, new Color(155, 89, 182));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 65));

        JLabel titleLabel = new JLabel("VoxDesk - Java Voice Assistant");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(10, 20, 10, 20));

        header.add(titleLabel, BorderLayout.WEST);
        return header;
    }

    private JPanel createControlPanel() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        controls.setBackground(new Color(245, 245, 245));
        controls.setBorder(new EmptyBorder(8, 20, 8, 20));

        styleButton(startButton, new Color(46, 204, 113));
        styleButton(stopButton, new Color(231, 76, 60));

        JLabel statusTitle = new JLabel("Status:");
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(new Color(41, 128, 185));

        controls.add(startButton);
        controls.add(stopButton);
        controls.add(Box.createHorizontalStrut(15));
        controls.add(statusTitle);
        controls.add(statusLabel);

        return controls;
    }

    private JPanel createLeftPanel() {
        JPanel left = new JPanel(new BorderLayout(0, 10));
        left.setBackground(Color.WHITE);

        JPanel partialPanel = new RoundedPanel(12);
        partialPanel.setLayout(new BorderLayout());
        partialPanel.setBackground(new Color(245, 250, 255));
        partialPanel.setBorder(BorderFactory.createTitledBorder("🎧 Currently Hearing (Live)"));
        partialLabel.setBorder(new EmptyBorder(8, 12, 8, 12));
        partialPanel.add(partialLabel, BorderLayout.CENTER);

        JPanel logPanel = new RoundedPanel(12);
        logPanel.setLayout(new BorderLayout());
        logPanel.setBackground(Color.WHITE);
        logPanel.setBorder(BorderFactory.createTitledBorder("🧠 Activity Log"));
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        left.add(partialPanel, BorderLayout.NORTH);
        left.add(logPanel, BorderLayout.CENTER);

        return left;
    }

    private JPanel createRightPanel() {
        JPanel right = new RoundedPanel(12);
        right.setLayout(new BorderLayout());
        right.setBackground(Color.WHITE);
        right.setBorder(BorderFactory.createTitledBorder("🗒 Available Commands"));
        right.add(new JScrollPane(commandsArea), BorderLayout.CENTER);
        return right;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(bgColor.darker(), 1, true));
        button.setPreferredSize(new Dimension(160, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    public void setController(AssistantController controller) {
        this.controller = controller;
    }

    private void wireEvents() {
        startButton.addActionListener(e -> {
            if (controller != null) controller.startListening();
        });
        stopButton.addActionListener(e -> {
            if (controller != null) controller.stopListening();
        });
    }

    public void setStatus(String status) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(status));
    }

    public void setPartialText(String text) {
        SwingUtilities.invokeLater(() ->
                partialLabel.setText(text == null || text.isBlank() ? "..." : text));
    }

    public void appendLog(String line) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(line + System.lineSeparator());
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void setAvailableCommands(String commands) {
        SwingUtilities.invokeLater(() -> {
            commandsArea.setText(commands);
            commandsArea.setCaretPosition(0);
        });
    }

    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;

        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
