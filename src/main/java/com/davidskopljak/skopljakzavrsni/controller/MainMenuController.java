package com.davidskopljak.skopljakzavrsni.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;

public class MainMenuController {
    public void handleNewCase() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(CRMApplication.class.getResource("view-case-info.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            CRMApplication.getPrimaryStage().setTitle("New case");
            CRMApplication.getPrimaryStage().setScene(scene);
            CRMApplication.getPrimaryStage().show();
        }catch(IOException e){
            CRMApplication.log.error(e.getMessage());
            throw e;
        }
    }


}
