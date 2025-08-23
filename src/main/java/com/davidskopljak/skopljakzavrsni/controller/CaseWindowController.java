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
    private Case activeCase;

    public CaseWindowController() {
        this.stage = new Stage();
        this.stage.setTitle("Case Window");
        this.activeCase = null;

        loadScene("case.fxml", "Case info", CaseInfoController.class );
    }

    public CaseWindowController(Case activeCase) {
        this.activeCase = activeCase;
        this.stage = new Stage();
        this.stage.setTitle("Case Window");

        loadScene("case.fxml", "Case info", CaseInfoController.class);
        System.out.println("Case window controller created with case: " + activeCase.getId() + " - " + activeCase.getClient().getFirstName() + " " + activeCase.getClient().getLastName() + activeCase.getLocation().getAddress() + ", " + activeCase.getClientVehicle().getId());
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

    public Case getActiveCase() {
        return activeCase;
    }
    public void setActiveCase(Case activeCase) {
        this.activeCase = activeCase;
    }
    public Stage getStage() {
        return this.stage;
    }
}
