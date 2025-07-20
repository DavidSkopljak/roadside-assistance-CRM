package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Case;
import com.davidskopljak.skopljakzavrsni.interfaces.CaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CaseWindowController {
    private final Stage stage;
    private final Case activeCase;

    public CaseWindowController() {
        this.activeCase = new Case();
        this.stage = new Stage();
        this.stage.setTitle("Case Window");

        // Load initial scene
        loadScene("case.fxml", "Case info", CaseInfoController.class );

        stage.show();
    }

    public CaseWindowController(Case activeCase) {
        this.activeCase = activeCase;
        this.stage = new Stage();
        this.stage.setTitle("Case Window");

        // Load initial scene
        loadScene("case.fxml", "Case info", CaseInfoController.class);
        stage.show();
    }

    public <T> void loadScene(String sceneUrl, String title, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource(sceneUrl));
            Parent root = loader.load();

            T controller =  controllerClass.cast(loader.getController());

            if (controller instanceof CaseController child) {
                child.setCaseWindowController(this);
            }

            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Case getactiveCase() {
        return activeCase;
    }
}
