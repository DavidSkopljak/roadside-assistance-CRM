package com.davidskopljak.skopljakzavrsni.helpers;

import com.davidskopljak.skopljakzavrsni.controller.CRMApplication;
import com.davidskopljak.skopljakzavrsni.exceptions.AccountLoginException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MiscHelpers {

    private MiscHelpers() {}

    public static void showAlert(String msg) {
        showAlert(msg, Alert.AlertType.INFORMATION);
    }

    public static void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void loadScene(String fxmlFile, String title, Stage stage) {
        try {
            URL fxmlUrl = CRMApplication.class.getResource(fxmlFile);
            if (fxmlUrl == null) {
                CRMApplication.log.error("FXML file not found at: {} ", fxmlFile);
                throw new AccountLoginException("FXML file not found");
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Parent root = fxmlLoader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException | AccountLoginException e) {
            CRMApplication.log.error("IOException while loading FXML: {}", fxmlFile, e);
        }
    }

    public static Stage loadSceneInNewStage(String sceneUrl, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CRMApplication.class.getResource(sceneUrl));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            return stage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
