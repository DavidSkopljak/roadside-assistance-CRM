package com.davidskopljak.skopljakzavrsni.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class CaseMenuController {
    public void handleViewCaseInfo() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(CRMApplication.class.getResource("view-case-info.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            CRMApplication.getPrimaryStage().setTitle("View Case Info");
            CRMApplication.getPrimaryStage().setScene(scene);
            CRMApplication.getPrimaryStage().show();
        }catch(IOException e){
            CRMApplication.log.error(e.getMessage());
            throw e;
        }
    }

    public void handleViewServices() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(CRMApplication.class.getResource("view-services.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            CRMApplication.getPrimaryStage().setTitle("View services");
            CRMApplication.getPrimaryStage().setScene(scene);
            CRMApplication.getPrimaryStage().show();
        }catch(IOException e){
            CRMApplication.log.error(e.getMessage());
            throw e;
        }
    }

    public void handleViewLocationInfo() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(CRMApplication.class.getResource("view-location-info.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            CRMApplication.getPrimaryStage().setTitle("View location Info");
            CRMApplication.getPrimaryStage().setScene(scene);
            CRMApplication.getPrimaryStage().show();
        }catch(IOException e){
            CRMApplication.log.error(e.getMessage());
            throw e;
        }
    }

    public void handleSaveCase() {

    }
}
