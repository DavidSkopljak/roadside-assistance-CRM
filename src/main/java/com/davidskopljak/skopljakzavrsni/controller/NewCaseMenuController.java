package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import javafx.fxml.FXML;

public class NewCaseMenuController {
    @FXML
    private void handleCancelCase(){
        CRMApplication.clearCase();
        MiscHelpers.loadScene("main-menu.fxml", "Main menu");
    }
}
