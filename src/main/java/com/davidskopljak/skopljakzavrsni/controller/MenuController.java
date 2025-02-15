package com.davidskopljak.skopljakzavrsni.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;

public class MenuController {
    @FXML
    public void handleNewCase() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CRMApplication.class.getResource("new-case-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CRMApplication.getPrimaryStage().setTitle("New case");
        CRMApplication.getPrimaryStage().setScene(scene);
        CRMApplication.getPrimaryStage().show();
    }
}
