package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainViewController {
    @FXML
    private TableView<Case> casesTableView;

    @FXML
    private TableColumn<Case, Long> caseIdTableColumn;

    @FXML
    private TableColumn<Client, String> lastNameTableColumn;

    @FXML
    private TableColumn<Client, String> firstNameTableColumn;

    @FXML
    private TableColumn<VehicleModel, String> vehicleBrandTableColumn;

    @FXML
    private TableColumn<Vehicle, String> licensePlateTableColumn;

    @FXML
    private TableColumn<Location, String> locationTableColumn;

    @FXML
    private TableColumn<Operator, String> firstEditedOperatorTableColumn;

    @FXML
    private TableColumn<Operator, String> lastEditedOperatorTableColumn;

}
