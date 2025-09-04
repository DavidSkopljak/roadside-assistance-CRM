package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;

public class ManageDriversMenuController {

    public void handleExitManageDrivers(){
        MiscHelpers.loadScene("main-view.fxml", "Main view", CRMApplication.getPrimaryStage());
    }

}
