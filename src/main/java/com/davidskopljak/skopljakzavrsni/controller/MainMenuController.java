package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.HashSet;
import java.util.Set;

public class MainMenuController {
    @FXML
    private MenuBar menuBar;

    private Set<CaseWindowController> caseWindowControllers = new HashSet<>();

    public void handleNewCase() {
        try {
            caseWindowControllers.add(new CaseWindowController());
        } catch (Exception e) {
            CRMApplication.log.error("Failed to open new case window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initialize() {
        if(CRMApplication.getActiveOperator().getUsername().equals("admin")) {
            Menu adminMenu = new Menu("Admin options");

            MenuItem addRemoveDrivers = new MenuItem("Add or remove drivers");
            MenuItem addRemoveWorkshops = new MenuItem("Add or remove workshops");
            MenuItem addRemoveOperators = new MenuItem("Add or remove operators");

            addRemoveDrivers.setOnAction(e -> handleAddRemoveDrivers());
            addRemoveWorkshops.setOnAction(e -> handleAddRemoveWorkshops());
            addRemoveOperators.setOnAction(e -> handleAddRemoveOperators());

            adminMenu.getItems().addAll(addRemoveDrivers, addRemoveWorkshops);

            menuBar.getMenus().add(adminMenu);
        }
    }

    private void handleAddRemoveDrivers() {
        MiscHelpers.loadScene("manage-drivers.fxml", "Manage drivers", CRMApplication.getPrimaryStage());
    }

    private void handleAddRemoveWorkshops() {
        MiscHelpers.loadScene("manage-workshops.fxml", "Manage workshops", CRMApplication.getPrimaryStage());
    }

    private void handleAddRemoveOperators(){
        MiscHelpers.loadScene("manage-operators.fxml", "Manage operators", CRMApplication.getPrimaryStage());
    }

}
