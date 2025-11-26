package com.example.voiceassistant.exceptions;

/**
 * Initialization/configuration errors.
 */
public class SetupException extends AssistantException {
    public SetupException(String message) { super(message); }
    public SetupException(String message, Throwable cause) { super(message, cause); }
}
