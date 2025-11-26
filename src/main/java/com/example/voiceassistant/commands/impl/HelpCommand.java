package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;

/**
 * Displays available commands and usage.
 * Usage: "help"
 */
public class HelpCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        return "=== Voice Assistant Commands ===\n\n" +
                "FILE MANAGEMENT:\n" +
                "  • show files - List files in current directory\n" +
                "  • open file <name> - Open a file (e.g., 'open file document.txt')\n" +
                "  • create file <name> - Create a new file (e.g., 'create file notes.txt')\n" +
                "  • delete file <name> - Delete a file (e.g., 'delete file oldfile.txt')\n\n" +
                "SYSTEM CONTROL:\n" +
                "  • shutdown - Shutdown computer\n" +
                "  • restart - Restart computer\n" +
                "  • lock screen - Lock the screen\n" +
                "  • volume <0-100> - Set volume level (e.g., 'volume 50')\n" +
                "  • brightness <0-100> - Set screen brightness (e.g., 'brightness 75')\n\n" +
                "MEDIA & APPLICATIONS:\n" +
                "  • open <app> - Open application (e.g., 'open notepad', 'open calculator')\n" +
                "  • search <query> - Search the web\n" +
                "  • screenshot - Take a screenshot (saves to Pictures/screenshots)\n" +
                "  • copy <text> - Copy text to clipboard\n\n" +
                "UTILITIES:\n" +
                "  • system info - Display system information\n" +
                "  • calculate <expression> - Perform calculations (e.g., 'calculate 5 plus 3')\n" +
                "  • ip address - Show network information\n" +
                "  • time - Get current time\n" +
                "  • note <text> - Save a note (e.g., 'note meeting at 3pm')\n" +
                "  • open browser - Open web browser\n" +
                "  • help - Show this help message\n";
    }
}
