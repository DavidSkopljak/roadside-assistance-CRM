package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.BufferableEntity;
import com.davidskopljak.skopljakzavrsni.entity.Case;
import com.davidskopljak.skopljakzavrsni.entity.EntityBuffer;
import com.davidskopljak.skopljakzavrsni.enums.BufferedChangeType;
import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class CaseMenuController{
    private CaseWindowController caseWindowController;

    @FXML
    MenuItem resolveCaseMenuItem;
    @FXML
    MenuItem cancelCaseMenuItem;
    @FXML
    MenuItem viewServicesMenuItem;
    @FXML
    MenuItem reactivateCaseMenuItem;
    @FXML
    MenuItem viewCaseNotesMenuItem;

    private String title = "View case info";
    private String caseInfoPath = "case-info.fxml";
    
    public void handleViewCaseInfo(){caseWindowController.loadScene(caseInfoPath, title,  CaseInfoController.class);}

    public void handleViewLocationInfo() {caseWindowController.loadScene("case-location.fxml", "View location info",  CaseLocationController.class);}

    public void handleSaveCase() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Save");
        confirmAlert.setHeaderText("Save Case");
        confirmAlert.setContentText("Are you sure you want to save this case?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try{
                    EntityBuffer<Long, Case> caseBuffer = CRMApplication.getCaseBuffer();
                    Case caseToSave = caseWindowController.getActiveCaseBuilder().build();
                    caseToSave.updateState(CaseState.ACTIVE);

                    if(!validateCase(caseToSave)){
                        MiscHelpers.showAlert("Case is not valid.", Alert.AlertType.ERROR);
                        return;
                    }

                    if(caseToSave.getId() == null){
                        caseBuffer.writeEntity(new BufferableEntity<>(null, caseToSave, CRMApplication.getActiveOperator(), BufferedChangeType.NEW));
                        caseWindowController.loadScene(caseInfoPath, title,  CaseInfoController.class);
                    } else {
                        caseBuffer.writeEntity(new BufferableEntity<>(caseToSave.getId(), caseToSave, CRMApplication.getActiveOperator(), BufferedChangeType.UPDATED));
                        caseWindowController.loadScene(caseInfoPath, title,  CaseInfoController.class);
                    }

                    caseWindowController.setActiveCase(caseToSave);
                }catch(RepositoryAccessException | EmptyResultSetException e){
                    CRMApplication.log.error("Failed to save or update case: ", e);
                    MiscHelpers.showAlert("Failed to save case. Please try again.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    public void handleCancelCase() {
        this.caseWindowController.getActiveCase().updateState(CaseState.CANCELLED);
        handleSaveCase();
    }


    public void handleResolveCase() {
        this.caseWindowController.getActiveCase().updateState(CaseState.RESOLVED);
        handleSaveCase();
    }

    public void handleReactivateCase(){
        this.caseWindowController.getActiveCase().updateState(CaseState.ACTIVE);
        handleSaveCase();
    }

    public void handleExitCase() {
        caseWindowController.getStage().close();
    }

    public void handleViewServices() {
            caseWindowController.loadScene("services-list.fxml", "View services", ServicesListController.class );
    }

    public void setCaseWindowController(CaseWindowController controller) {
        this.caseWindowController = controller;
        refreshEditableState();
    }

    public void handleViewCaseNotes(){
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("case-notes.fxml"));
            Parent root = loader.load();
            caseWindowController.getStage().setScene(new Scene(root));
            caseWindowController.getStage().setTitle("Case notes");
            caseWindowController.getStage().show();
            CaseNotesController caseNotesController = loader.getController();
            caseNotesController.setCaseWindowController(this.caseWindowController);
        } catch (IOException e) {
            CRMApplication.log.error("Failed to load case notes: ", e);
        }
    }

    public void refreshEditableState(){
        if(caseWindowController.getActiveCase() == null){
            resolveCaseMenuItem.setDisable(true);
            cancelCaseMenuItem.setDisable(true);
            reactivateCaseMenuItem.setDisable(true);
            viewCaseNotesMenuItem.setDisable(true);
            viewServicesMenuItem.setDisable(true);
        } else if(caseWindowController.getActiveCase().getState() == CaseState.RESOLVED
                || caseWindowController.getActiveCase().getState() == CaseState.CANCELLED ){
            resolveCaseMenuItem.setDisable(true);
            cancelCaseMenuItem.setDisable(true);
            reactivateCaseMenuItem.setDisable(false);
            viewCaseNotesMenuItem.setDisable(false);
            viewServicesMenuItem.setDisable(false);
        } else if(caseWindowController.getActiveCase().getState() == CaseState.ACTIVE){
            resolveCaseMenuItem.setDisable(false);
            cancelCaseMenuItem.setDisable(false);
            reactivateCaseMenuItem.setDisable(true);
            viewCaseNotesMenuItem.setDisable(false);
            viewServicesMenuItem.setDisable(false);
        }
    }

    private boolean validateCase(Case activeCase) {
        boolean valid = true;

        if (activeCase == null) {
            valid = false;
            return valid;
        }

        if (activeCase.getClient() == null) {
            valid = false;
        }
        if (activeCase.getLocation() == null) {
            valid = false;
        }
        if (activeCase.getClientVehicle() == null) {
            valid = false;
        }
        if (activeCase.getFirstOperator() == null) {
            valid = false;
        }
        if (activeCase.getLastEditedOperator() == null) {
            valid = false;
        }
        if (activeCase.getState() == null) {
            valid = false;
        }
        if (activeCase.getDamageCause() == null) {
            valid = false;
        }
        if (activeCase.getDamageDescription() == null) {
            valid = false;
        }
        if (activeCase.getDamageType() == null) {
            valid = false;
        }
        if (activeCase.getClientVehicleFirstRegistrationDate() == null) {
            valid = false;
        }

        return valid;
    }

}
