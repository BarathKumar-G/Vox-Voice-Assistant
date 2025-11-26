package com.example.voiceassistant.commands;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple registry for mapping intent keywords to command instances.
 */
public class CommandRegistry {
    private final Map<String, Command> map = new HashMap<>();

    public void register(String key, Command cmd) {
        map.put(key, cmd);
    }

    public Command get(String key) {
        return map.get(key);
    }
}
