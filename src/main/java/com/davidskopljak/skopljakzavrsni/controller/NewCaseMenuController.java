package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;
import javafx.event.ActionEvent;

import java.sql.SQLException;

public class NewCaseMenuController {
    public void handleViewCaseInfo(){
        MiscHelpers.loadScene("new-case.fxml", "View case info");
    }

    public void handleViewLocationInfo() {
        MiscHelpers.loadScene("new-case-location.fxml", "View location");
    }

    public void handleSaveCase() {
        try{
            CaseRepository caseRepository = new CaseRepository();
            CRMApplication.getCaseInProgress().setCaseState(CaseState.ACTIVE);
            caseRepository.save(CRMApplication.getCaseInProgress());
            CRMApplication.clearCaseInProgress();
            MiscHelpers.loadScene("main-view.fxml", "Main menu");
        }catch(SQLException e){
            throw new RepositoryAccessException("Failed to save new case: " + e);
        }
    }

    public void handleCancelCase() {
        CRMApplication.clearCaseInProgress();
        MiscHelpers.loadScene("main-view.fxml", "Main menu");
    }
}
