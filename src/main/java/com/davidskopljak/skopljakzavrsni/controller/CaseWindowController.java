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
    private Case.Builder activeCaseBuilder = new Case.Builder();

    public CaseWindowController() {
        this.stage = new Stage();
        this.stage.setTitle("Case Window");
        this.activeCase = null;

        loadScene("case-info.fxml", "Case info", CaseInfoController.class );
    }

    public CaseWindowController(Case activeCase) {
        this.activeCase = activeCase;
        this.activeCaseBuilder = activeCase.getPopulatedBuilder();
        this.stage = new Stage();
        this.stage.setTitle("Case Window");

        loadScene("case-info.fxml", "Case info", CaseInfoController.class);
        stage.show();
    }

    public <T extends CaseController> void loadScene(String sceneUrl, String title, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource(sceneUrl));
            Parent root = loader.load();

            T controller =  controllerClass.cast(loader.getController());

            if (controller instanceof CaseController child) {
                System.out.println("Setting CaseWindowController controller for " + sceneUrl);
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
    public Case.Builder getActiveCaseBuilder() {
        return activeCaseBuilder;
    }
    public void setActiveCase(Case activeCase) {
        this.activeCase = activeCase;
    }
    public Stage getStage() {
        return this.stage;
    }
}
