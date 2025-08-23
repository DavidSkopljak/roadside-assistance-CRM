package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;

public class MainMenuController {
    public void handleNewCase() {
        try {
            CaseWindowController caseWindowController = new CaseWindowController();
        } catch (Exception e) {
            CRMApplication.log.error("Failed to open new case window: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
