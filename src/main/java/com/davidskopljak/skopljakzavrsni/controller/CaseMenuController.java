package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Case;
import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    public void handleViewCaseInfo(){caseWindowController.loadScene("case-info.fxml", "View case info",  CaseInfoController.class);}

    public void handleViewLocationInfo() {caseWindowController.loadScene("case-location.fxml", "View location info",  CaseLocationController.class);}

    public void handleSaveCase() {
        try{
            CaseRepository caseRepository = new CaseRepository();
            Case caseToSave = caseWindowController.getActiveCaseBuilder().build();
            caseToSave.updateState(CaseState.ACTIVE);


            if(!validateCase(caseToSave)){
               MiscHelpers.showAlert("Case is not valid.", Alert.AlertType.ERROR);
               return;
            }

            if(caseToSave.getId() == null){
                Long caseId = caseRepository.save(caseToSave);
                caseToSave.setId(caseId);
                caseWindowController.loadScene("case-info.fxml", "View case info",  CaseInfoController.class);
            }

            caseWindowController.setActiveCase(caseToSave);
        }catch(RepositoryAccessException | EmptyResultSetException e){
            CRMApplication.log.error("Failed to save or update case: ", e);
            MiscHelpers.showAlert("Failed to save or update case: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("services-list.fxml"));
            Parent root = loader.load();
            caseWindowController.getStage().setScene(new Scene(root));
            caseWindowController.getStage().setTitle("View services");
            caseWindowController.getStage().show();
            ServicesListController servicesListController = loader.getController();
            servicesListController.setCaseWindowController(this.caseWindowController);
        } catch (IOException e) {
            CRMApplication.log.error("Failed to load services list: ", e);
        }
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
        } else if(caseWindowController.getActiveCase().getState() == CaseState.RESOLVED
                || caseWindowController.getActiveCase().getState() == CaseState.CANCELLED ){
            resolveCaseMenuItem.setDisable(true);
            cancelCaseMenuItem.setDisable(true);
            reactivateCaseMenuItem.setDisable(false);
            viewCaseNotesMenuItem.setDisable(false);
        } else if(caseWindowController.getActiveCase().getState() == CaseState.ACTIVE){
            resolveCaseMenuItem.setDisable(false);
            cancelCaseMenuItem.setDisable(false);
            reactivateCaseMenuItem.setDisable(true);
            viewCaseNotesMenuItem.setDisable(false);
        }
    }

    /*private boolean validateCase(){
        Case activeCase = caseWindowController.getActiveCase();
        if(activeCase == null){
            MiscHelpers.showAlert("Case is not selected.", Alert.AlertType.ERROR);
            return false;
        } else return activeCase.getClient() != null
                && activeCase.getLocation() != null
                && activeCase.getClientVehicle() != null
                && activeCase.getFirstOperator() != null
                && activeCase.getLastEditedOperator() != null
                && activeCase.getState() != null
                && activeCase.getDamageCause() != null
                && activeCase.getDamageDescription() != null
                && activeCase.getDamageType() != null
                && activeCase.getClientVehicleFirstRegistrationDate() != null;
    }*/

    private boolean validateCase(Case activeCase) {
        if (activeCase == null) {
            System.out.println("Active case is null.");
            MiscHelpers.showAlert("Case is not selected.", Alert.AlertType.ERROR);
            return false;
        }

        boolean valid = true;

        if (activeCase.getClient() == null) {
            System.out.println("Client is null.");
            valid = false;
        }
        if (activeCase.getLocation() == null) {
            System.out.println("Location is null.");
            valid = false;
        }
        if (activeCase.getClientVehicle() == null) {
            System.out.println("Client vehicle is null.");
            valid = false;
        }
        if (activeCase.getFirstOperator() == null) {
            System.out.println("First operator is null.");
            valid = false;
        }
        if (activeCase.getLastEditedOperator() == null) {
            System.out.println("Last edited operator is null.");
            valid = false;
        }
        if (activeCase.getState() == null) {
            System.out.println("Case state is null.");
            valid = false;
        }
        if (activeCase.getDamageCause() == null) {
            System.out.println("Damage cause is null.");
            valid = false;
        }
        if (activeCase.getDamageDescription() == null) {
            System.out.println("Damage description is null.");
            valid = false;
        }
        if (activeCase.getDamageType() == null) {
            System.out.println("Damage type is null.");
            valid = false;
        }
        if (activeCase.getClientVehicleFirstRegistrationDate() == null) {
            System.out.println("Client vehicle first registration date is null.");
            valid = false;
        }

        return valid;
    }

}
