package com.davidskopljak.skopljakzavrsni.interfaces;

import com.davidskopljak.skopljakzavrsni.entity.Case;
import com.davidskopljak.skopljakzavrsni.entity.Note;
import com.davidskopljak.skopljakzavrsni.entity.Service;

import java.util.List;

public sealed interface Noteable permits Case, Service {
    public List<Note> getNotes();
    public void addNotes(List<Note> notes);
    public void addNote(Note note);
}
