package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Takes a screenshot, saves it to Pictures/screenshots, and opens it immediately.
 * Usage: "screenshot"
 */
public class ScreenshotCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        try {
            String userHome = System.getProperty("user.home");
            String screenshotDir = userHome + File.separator + "Pictures" + File.separator + "screenshots";

            // Create directory if it doesn't exist
            File dir = new File(screenshotDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Capture screen
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);

            // Generate filename with timestamp
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String filename = "screenshot_" + timestamp + ".png";
            File outputFile = new File(dir, filename);

            // Save screenshot
            ImageIO.write(capture, "png", outputFile);

            String filePath = outputFile.getAbsolutePath();
            openFile(filePath);

            return "Screenshot saved and opened: " + filePath;
        } catch (Exception e) {
            return "Failed to take screenshot: " + e.getMessage();
        }
    }

    private void openFile(String filePath) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("win")) {
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", filePath});
        } else if (os.contains("mac")) {
            Runtime.getRuntime().exec(new String[]{"open", filePath});
        } else {
            // Linux
            Runtime.getRuntime().exec(new String[]{"xdg-open", filePath});
        }
    }
}
