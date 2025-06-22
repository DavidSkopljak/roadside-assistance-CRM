package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import javafx.event.ActionEvent;

public class NewCaseMenuController {
    public void handleViewCaseInfo(){
        CRMApplication.getSharedNewCaseController().outputForm();
        MiscHelpers.loadScene("new-case.fxml", "View case info");
    }

    public void handleViewLocationInfo() {
        CRMApplication.getSharedNewCaseController().outputForm();
        MiscHelpers.loadScene("new-case-location.fxml", "View location");
    }

    public void handleSaveCase() {

    }

    public void handleCancelCase(ActionEvent actionEvent) {
    }
}
