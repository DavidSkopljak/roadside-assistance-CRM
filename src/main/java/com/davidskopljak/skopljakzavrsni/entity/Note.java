package com.davidskopljak.skopljakzavrsni.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Note extends Entity {
    private String noteText;
    private LocalDateTime noteDateTime;

    public Note(Long id, String noteText, LocalDateTime noteDateTime) {
        super(id);
        this.noteText = noteText;
        this.noteDateTime = noteDateTime;
    }

    public Note(String noteText, LocalDateTime noteDateTime) {
        this.noteText = noteText;
        this.noteDateTime = noteDateTime;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public LocalDateTime getNoteDateTime() {
        return noteDateTime;
    }

    public void setNoteDateTime(LocalDateTime noteDateTime) {
        this.noteDateTime = noteDateTime;
    }
}
