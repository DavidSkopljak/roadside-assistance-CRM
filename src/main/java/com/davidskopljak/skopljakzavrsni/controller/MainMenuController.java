package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;

public class MainMenuController {
    public void handleNewCase() {
        MiscHelpers.loadScene("case.fxml", "New case");
    }
}
