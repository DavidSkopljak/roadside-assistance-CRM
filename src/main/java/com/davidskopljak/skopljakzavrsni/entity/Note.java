package com.davidskopljak.skopljakzavrsni.entity;

import java.time.Instant;

public class Note {
    private String message;
    private Instant timestampUTC;
    private Long caseId;

    public Note(String message, Instant timestamp, Long caseId) {
        this.message = message;
        this.timestampUTC = timestamp;
        this.caseId = caseId;
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

    public Long getCaseId() {return caseId;}
    public void setCaseId(Long caseId) {this.caseId = caseId;}
}