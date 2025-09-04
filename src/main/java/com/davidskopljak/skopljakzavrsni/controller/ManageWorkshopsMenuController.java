package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;

public class ManageWorkshopsMenuController {
    public void handleExitManageWorkshops(){
        MiscHelpers.loadScene("main-view.fxml", "Main view", CRMApplication.getPrimaryStage());
    }
}
