package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Returns the current system time.
 */
public class SystemTimeCommand implements Command {
    @Override
    public String execute(String arg) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "The time is " + time;
    }
}
