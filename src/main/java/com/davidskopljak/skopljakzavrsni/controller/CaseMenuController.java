package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class CaseMenuController {
    public void handleViewCaseInfo() {
        MiscHelpers.loadScene("case.fxml", "View case info");
    }

    public void handleViewServices() {
        MiscHelpers.loadScene("case-services.fxml", "View services");
    }

    public void handleViewLocationInfo(){
        MiscHelpers.loadScene("case-location.fxml", "View location");
    }

    public void handleSaveCase() {

    }

    public void handleNewService(ActionEvent actionEvent) {
    }

    public void handleResolveCase(ActionEvent actionEvent) {
    }

    public void handleCancelCase(ActionEvent actionEvent) {
    }


}
