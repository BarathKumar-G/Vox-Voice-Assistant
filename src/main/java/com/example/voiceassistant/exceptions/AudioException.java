package com.example.voiceassistant.exceptions;

/**
 * Audio setup/streaming errors.
 */
public class AudioException extends AssistantException {
    public AudioException(String message) { super(message); }
    public AudioException(String message, Throwable cause) { super(message, cause); }
}
