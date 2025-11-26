package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;

/**
 * Displays system information.
 * Usage: "system info"
 */
public class SystemInfoCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        StringBuilder info = new StringBuilder();

        // OS Information
        info.append("=== System Information ===\n");
        info.append("OS: ").append(System.getProperty("os.name")).append("\n");
        info.append("OS Version: ").append(System.getProperty("os.version")).append("\n");
        info.append("Architecture: ").append(System.getProperty("os.arch")).append("\n");

        // Java Information
        info.append("\n=== Java Information ===\n");
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("Java Vendor: ").append(System.getProperty("java.vendor")).append("\n");

        // Memory Information
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long heapMax = memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024);

        info.append("\n=== Memory Information ===\n");
        info.append("Heap Memory Used: ").append(heapUsed).append(" MB\n");
        info.append("Heap Memory Max: ").append(heapMax).append(" MB\n");

        // CPU Information
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        info.append("\n=== CPU Information ===\n");
        info.append("Available Processors: ").append(osBean.getAvailableProcessors()).append("\n");
        info.append("System Load Average: ").append(String.format("%.2f", osBean.getSystemLoadAverage())).append("\n");

        // User Information
        info.append("\n=== User Information ===\n");
        info.append("User Name: ").append(System.getProperty("user.name")).append("\n");
        info.append("User Home: ").append(System.getProperty("user.home")).append("\n");

        return info.toString();
    }
}
