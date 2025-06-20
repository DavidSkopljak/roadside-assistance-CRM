package com.davidskopljak.skopljakzavrsni.entity;

import java.time.Instant;

public class Note {
    private String message;
    private Instant timestampUTC;

    public Note(String message, Instant timestamp) {
        this.message = message;
        this.timestampUTC = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestampUTC;
    }

    public void setTimestamp(Instant timestampUTC) {
        this.timestampUTC = timestampUTC;
    }
}