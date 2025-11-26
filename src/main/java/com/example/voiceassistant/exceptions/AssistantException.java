package com.example.voiceassistant.exceptions;

/**
 * Base exception type for assistant-related errors.
 */
public class AssistantException extends Exception {
    public AssistantException(String message) { super(message); }
    public AssistantException(String message, Throwable cause) { super(message, cause); }
}
