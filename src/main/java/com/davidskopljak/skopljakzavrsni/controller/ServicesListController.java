package com.davidskopljak.skopljakzavrsni.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ServicesListController {
    private ServicesListMenuController servicesListMenuController;
    private Long caseId;

    @FXML
    private AnchorPane rootAnchorPane;

    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("services-list-menu.fxml"));
            Parent menuRoot = loader.load();
            this.servicesListMenuController = loader.getController();
            this.servicesListMenuController.setServicesListController(this);
            if(this.caseId != null) {
                this.servicesListMenuController.setCaseId(this.caseId);
            }
            rootAnchorPane.getChildren().addFirst(menuRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCaseId(Long caseId){
        this.caseId = caseId;
    }
}
