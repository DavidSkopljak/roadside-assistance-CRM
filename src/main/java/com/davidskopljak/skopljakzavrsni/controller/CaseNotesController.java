package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Note;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.repository.NoteRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class CaseNotesController {
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private VBox notesVbox;

    private CaseWindowController caseWindowController;
    private List<Note> caseNotes;

    private void addTextArea() {
        TextArea input = new TextArea();
        input.setPrefHeight(30);
        input.setWrapText(true);
        input.setMaxWidth(1190);

        input.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                event.consume();
                String text = input.getText().trim();
                if (!text.isEmpty()) {
                    TextArea note = new TextArea(text);
                    note.setPrefHeight(30);
                    note.setWrapText(true);
                    note.setMaxWidth(1190);
                    note.setEditable(false);

                    this.notesVbox.getChildren().add(1, note);

                    input.clear();

                    try{
                        NoteRepository noteRepository = new NoteRepository();
                        noteRepository.save(new Note(note.getText(), caseWindowController.getActiveCase().getId()));
                    } catch (RepositoryAccessException e){
                        CRMApplication.log.error("Failed to save new note: ", e);
                        this.notesVbox.getChildren().remove(note);
                        input.setText(text);
                        input.requestFocus();
                    }
                }
            }
        });

        this.notesVbox.getChildren().addFirst(input);
    }

    private void initializeCaseNotes(){
        try{
            NoteRepository noteRepository = new NoteRepository();
            Long caseId = this.caseWindowController.getActiveCase().getId();
            this.caseNotes = noteRepository.findAllByCaseId(caseId);
            showExistingNotes();

        } catch(RepositoryAccessException e){
            CRMApplication.log.error("Failed to load case notes: ", e);
        }

    }

    private void showExistingNotes() {
        if (this.caseNotes == null || this.caseNotes.isEmpty()) {
            return;
        }

        for (Note note : caseNotes) {
            TextArea noteArea = new TextArea(note.getMessage());
            noteArea.setPrefHeight(30);
            noteArea.setWrapText(true);
            noteArea.setMaxWidth(1190);
            noteArea.setEditable(false);

            if (!notesVbox.getChildren().isEmpty()) {
                notesVbox.getChildren().add(1, noteArea);
            } else {
                notesVbox.getChildren().add(noteArea);
            }
        }
    }

    private void injectCaseNotesMenuController() {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("case-notes-menu.fxml"));
            Parent menuRoot = loader.load();
            CaseNotesMenuController caseNotesMenuController = loader.getController();
            caseNotesMenuController.setCaseNotesController(this);
            rootAnchorPane.getChildren().addFirst(menuRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCaseWindowController(CaseWindowController caseWindowController){
        this.caseWindowController = caseWindowController;
        injectCaseNotesMenuController();
        initializeCaseNotes();
        addTextArea();
    }

    public CaseWindowController getCaseWindowController(){
        return this.caseWindowController;
    }
}
