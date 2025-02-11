package com.davidskopljak.skopljakzavrsni.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

public class FirstViewController {

    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu newCaseMenu;


    public void handleNewCase(){
        System.out.println("New case");
    }
}
