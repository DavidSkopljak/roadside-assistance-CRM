package com.davidskopljak.skopljakzavrsni.entity;

import java.time.Instant;

public class Note extends Entity{
    private String message;
    private Instant timestampUTC;
    private Long caseId;

    public Note(Long id, String message, Long caseId) {
        super(id);
        this.message = message;
        this.caseId = caseId;
    }

    public Note(String message, Long caseId) {
        this.message = message;
        this.caseId = caseId;
    }

    public Note(Long id, Note note) {
        super(id);
        this.message = note.getMessage();
        this.timestampUTC = note.getTimestamp();
        this.caseId = note.getCaseId();
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