package com.davidskopljak.skopljakzavrsni.interfaces;

import com.davidskopljak.skopljakzavrsni.entity.Case;
import com.davidskopljak.skopljakzavrsni.entity.Service;

import java.util.List;

public sealed interface Noteable permits Case, Service {
    public List<String> getNotes();
    public void setNotes(List<String> notes);
}
