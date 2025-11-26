package com.example.voiceassistant.commands;

/**
 * Command abstraction: returns a result string for display/log, may be null.
 */
public interface Command {
    String execute(String arg) throws Exception;
}
