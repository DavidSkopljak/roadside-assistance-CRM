package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
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

    public void handleCancelCase() { /*TODO*/ }

    public void handleExitCase() {
        caseWindowController.getStage().close();
    }

    public void handleResolveCase() { /*TODO*/ }


    public void handleViewServices() {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("services-list.fxml"));
            Parent root = loader.load();
            caseWindowController.getStage().setScene(new Scene(root));
            caseWindowController.getStage().setTitle("View services");
            caseWindowController.getStage().show();
            ServicesListController servicesListController = loader.getController();
            servicesListController.setCaseId(this.caseWindowController.getActiveCase().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCaseWindowController(CaseWindowController controller) {
        System.out.println("Setting case window controller inside CaseMenuController: " + controller.getClass().getSimpleName());
        this.caseWindowController = controller;
    }
}
