package com.davidskopljak.skopljakzavrsni.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServicesListMenuController {
    private ServicesListController servicesListController;
    private Long caseId;

    public void handleNewService() {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("service.fxml"));
            Parent root = loader.load();
            if (this.caseId != null) {
                ServiceController serviceController = loader.getController();
                serviceController.setCaseId(this.caseId);
                serviceController.setServicesListController(this.servicesListController);
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("New service");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBackToCase() {}

    public void setServicesListController(ServicesListController controller) {
        this.servicesListController = controller;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }
}
