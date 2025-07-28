package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.exceptions.AccountLoginException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;

public class MainViewController {
    @FXML
    private TableView<Case> casesTableView;

    @FXML
    private TableColumn<Case, Long> caseIdTableColumn;

    @FXML
    private TableColumn<Case, String> lastNameTableColumn;

    @FXML
    private TableColumn<Case, String> firstNameTableColumn;

    @FXML
    private TableColumn<Case, String> vehicleBrandTableColumn;

    @FXML
    private TableColumn<Case, String> licensePlateTableColumn;

    @FXML
    private TableColumn<Case, String> locationTableColumn;

    @FXML
    private TableColumn<Case, String> firstEditedOperatorTableColumn;

    @FXML
    private TableColumn<Case, String> lastEditedOperatorTableColumn;

    public void initialize() {
        caseIdTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleLongProperty(cellData.getValue().getId()).asObject());

        lastNameTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClient().getLastName()));

        firstNameTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClient().getFirstName()));

        vehicleBrandTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClientVehicle().getModel().toString()));

        locationTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLocation().getAddress() + ", " + cellData.getValue().getLocation().getCity()));

        licensePlateTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClientVehicle().getLicensePlate()));

        firstEditedOperatorTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFirstOperator().getFirstName() + " " + cellData.getValue().getFirstOperator().getLastName()));

        lastEditedOperatorTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLastEditedOperator().getFirstName() + " " + cellData.getValue().getLastEditedOperator().getLastName()));

        try{
            CaseRepository caseRepository = new CaseRepository();
            casesTableView.getItems().addAll(caseRepository.findAll());
        } catch (Exception e) {
            throw new RepositoryAccessException("Failed to load cases: " + e);
        }

        casesTableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Case selectedCase = casesTableView.getSelectionModel().getSelectedItem();
                if (selectedCase != null) {
                    try {
                        CaseWindowController caseWindowController = new CaseWindowController(selectedCase);
                    } catch (Exception e) {
                        CRMApplication.log.error("Failed to open existing case window: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}
