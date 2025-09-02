package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Case;
import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class ServicesListMenuController {
    @FXML
    MenuItem newServiceMenuItem;

    private ServicesListController servicesListController;

    public void handleNewService() {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("service.fxml"));
            Parent root = loader.load();
            ServiceController serviceController = loader.getController();
            Long caseId = this.servicesListController.getCaseWindowController().getActiveCase().getId();
            if (caseId != null) {
                serviceController.setCaseId(caseId);
            }
            serviceController.setActiveService(null);
            Stage stage = new Stage();
            serviceController.setStage(stage);
            stage.setScene(new Scene(root));
            stage.setTitle("New service");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBackToCase() {
        Stage stage = this.servicesListController.getCaseWindowController().getStage();
        stage.setTitle("Case Window");

        this.servicesListController.getCaseWindowController().loadScene("case-info.fxml", "Case info", CaseInfoController.class);
        stage.show();
    }

    public void setServicesListController(ServicesListController servicesListController) {
        this.servicesListController = servicesListController;
    }

    public void refreshEditableState(){
        Case activeCase = this.servicesListController.getCaseWindowController().getActiveCase();
        newServiceMenuItem.setDisable(activeCase.getState() == CaseState.RESOLVED || activeCase.getState() == CaseState.CANCELLED);
    }
}
