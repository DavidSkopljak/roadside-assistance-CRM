package com.davidskopljak.skopljakzavrsni.helpers;

import com.davidskopljak.skopljakzavrsni.controller.CRMApplication;
import com.davidskopljak.skopljakzavrsni.controller.NewCaseController;
import com.davidskopljak.skopljakzavrsni.exceptions.AccountLoginException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.io.IOException;

public class MiscHelpers {

    public static void showAlert(String msg) {
        showAlert(msg, Alert.AlertType.INFORMATION);
    }

    public static void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void loadScene(String sceneUrl, String title){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CRMApplication.class.getResource(sceneUrl));

            if (sceneUrl.startsWith("new-case")) {
                System.out.println("Loading new case scene!!!!");
                fxmlLoader.setControllerFactory(param -> {
                    System.out.println("Controller factory asked for: " + param);
                    if (param == NewCaseController.class) {
                        System.out.println("Returning shared NewCaseController instance");
                        return CRMApplication.getSharedNewCaseController();
                    } else {
                        try {
                            return param.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } else if (sceneUrl.startsWith("case")) {
                // set shared controller if needed here
            }

            Parent root = fxmlLoader.load(); // load once here
            Scene scene = new Scene(root);
            CRMApplication.getPrimaryStage().setTitle(title);
            CRMApplication.getPrimaryStage().setScene(scene);
            CRMApplication.getPrimaryStage().show();
        } catch (IOException e) {
            CRMApplication.log.error(e.getMessage());
            throw new AccountLoginException("Could not load scene with URL: " + sceneUrl + "." + e);
        }
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
