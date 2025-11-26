package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

/**
 * Displays network information.
 * Usage: "ip address" or "network info"
 */
public class IPAddressCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        try {
            StringBuilder info = new StringBuilder();

            // Local IP
            InetAddress localhost = InetAddress.getLocalHost();
            info.append("Local IP Address: ").append(localhost.getHostAddress()).append("\n");
            info.append("Host Name: ").append(localhost.getHostName()).append("\n");

            // All network interfaces
            info.append("\n=== Network Interfaces ===\n");
            Collections.list(NetworkInterface.getNetworkInterfaces()).forEach(ni -> {
                try {
                    if (ni.isUp()) {
                        info.append("Interface: ").append(ni.getName()).append("\n");
                        Collections.list(ni.getInetAddresses()).forEach(addr ->
                                info.append("  IP: ").append(addr.getHostAddress()).append("\n")
                        );
                    }
                } catch (Exception e) {
                    // Skip
                }
            });

            return info.toString();
        } catch (Exception e) {
            return "Failed to get network info: " + e.getMessage();
        }
    }
}
