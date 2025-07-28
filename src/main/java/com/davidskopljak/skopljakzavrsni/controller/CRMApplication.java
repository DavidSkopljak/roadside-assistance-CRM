package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Operator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.IOException;


public class CRMApplication extends Application {
    public static final Logger log = LoggerFactory.getLogger(CRMApplication.class);
    private static Stage PRIMARY_STAGE;
    private static Operator ACTIVE_OPERATOR;

    @Override
    public void start(Stage stage){
        try{
            CRMApplication.setPrimaryStage(stage);
            FXMLLoader fxmlLoader = new FXMLLoader(CRMApplication.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1080, 600);
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

    public static void logIn(Operator operator){
        ACTIVE_OPERATOR = operator;
    }

    public static Stage getPrimaryStage() {return PRIMARY_STAGE;}
    private static void setPrimaryStage(Stage primaryStage) {CRMApplication.PRIMARY_STAGE = primaryStage;}
    public static Operator getActiveOperator() {return ACTIVE_OPERATOR;}
}