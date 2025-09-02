package com.davidskopljak.skopljakzavrsni.controller;

import javafx.stage.Stage;

public class CaseNotesMenuController {
    private CaseNotesController caseNotesController;

    public void handleReturnToCase(){
        Stage stage = this.caseNotesController.getCaseWindowController().getStage();
        stage.setTitle("Case Window");

        this.caseNotesController.getCaseWindowController().loadScene("case-info.fxml", "Case info", CaseInfoController.class);
        stage.show();
    }
    public void setCaseNotesController(CaseNotesController controller){
        this.caseNotesController = controller;
    }
}
