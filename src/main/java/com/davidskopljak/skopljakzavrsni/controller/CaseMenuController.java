package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;

import java.sql.SQLException;

public class CaseMenuController{
    private CaseWindowController caseWindowController;

    public void handleViewCaseInfo(){caseWindowController.loadScene("case.fxml", "View case info",  CaseInfoController.class);}

    public void handleViewLocationInfo() {caseWindowController.loadScene("case-location.fxml", "View case info",  CaseLocationController.class);}

    public void handleSaveCase() {
        try{
            CaseRepository caseRepository = new CaseRepository();
            caseWindowController.getActiveCase().setCaseState(CaseState.ACTIVE);
            if( caseWindowController.getActiveCase().getId() != null){
                System.out.println("Updating case: " + caseWindowController.getActiveCase().getId() + "with vehicle with id " + caseWindowController.getActiveCase().getClientVehicle().getId());
                caseRepository.update(caseWindowController.getActiveCase());
            }else {
                caseRepository.save(caseWindowController.getActiveCase());
            }

        }catch(SQLException e){
            throw new RepositoryAccessException("Failed to save new case: " + e);
        }
    }

    public void handleCancelCase() {
        MiscHelpers.loadScene("main-view.fxml", "Main menu");
    }

    public void setCaseWindowController(CaseWindowController controller) {
        System.out.println("Setting case window controller inside CaseMenuController: " + controller.getClass().getSimpleName());
        this.caseWindowController = controller;
    }
}
