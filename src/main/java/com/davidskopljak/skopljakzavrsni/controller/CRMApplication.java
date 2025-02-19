package com.davidskopljak.skopljakzavrsni.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.IOException;


public class    CRMApplication extends Application {
    public static final Logger log = LoggerFactory.getLogger(CRMApplication.class);
    private static Stage primaryStage;
    @Override
    public void start(Stage stage){
        try{
            CRMApplication.setPrimaryStage(stage);
            FXMLLoader fxmlLoader = new FXMLLoader(CRMApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("CRM Application");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            log.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getPrimaryStage() {return primaryStage;}
    private static void setPrimaryStage(Stage primaryStage) {CRMApplication.primaryStage = primaryStage;}
}