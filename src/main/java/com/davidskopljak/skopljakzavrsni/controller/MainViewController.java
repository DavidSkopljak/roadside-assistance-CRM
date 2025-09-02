package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

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

    @FXML
    private TextArea filterTextArea;

    @FXML
    private ComboBox<String> filterComboBox;


    List<Stage> openCases = new ArrayList<>();


    public void initialize() {
        filterComboBox.getItems().addAll(
                "Case ID",
                "Last name",
                "First name",
                "Vehicle brand",
                "License plate",
                "Location",
                "First edited operator",
                "Last edited operator"
        );

        filterTextArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Thread runner = new Thread(() -> {
                    try {
                        CaseRepository caseRepository = new CaseRepository();
                        List<Case> allCases = caseRepository.findAll();
                        final List<Case> results;

                        String selectedColumn = filterComboBox.getSelectionModel().getSelectedItem();
                        String filter = filterTextArea.getText().trim().toLowerCase();

                        if(selectedColumn == null){
                            results = matchAnyColumn(allCases, filter);
                        } else {
                            results = matchColumn(allCases, filter, selectedColumn);
                        }


                        javafx.application.Platform.runLater(() -> casesTableView.getItems().setAll(results));
                    } catch (Exception e) {
                        CRMApplication.log.error("Search failed", e);
                    }
                });

                runner.start();
            }
        });

        initializeCaseTable();

        try{
            CaseRepository caseRepository = new CaseRepository();
            casesTableView.getItems().addAll(caseRepository.findAll());
        } catch (Exception e) {
            throw new RepositoryAccessException("Failed to load cases: " + e);
        }
    }

    private void initializeCaseTable() {
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

        casesTableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Case selectedCase = casesTableView.getSelectionModel().getSelectedItem();
                if (selectedCase != null) {
                    try {
                        CaseWindowController caseWindowController = new CaseWindowController(selectedCase);
                        openCases.add(caseWindowController.getStage());
                    } catch (Exception e) {
                        CRMApplication.log.error("Failed to open existing case window: ", e);
                    }
                }
            }
        });
    }

    private List<Case> matchAnyColumn(List<Case> allCases, String filter){
        List<Case> results = new ArrayList<>();
        for(Case c : allCases){
            if (String.valueOf(c.getId()).contains(filter)
                    || c.getClient().getLastName().toLowerCase().contains(filter)
                    || c.getClient().getFirstName().toLowerCase().contains(filter)
                    || c.getClientVehicle().getModel().toString().toLowerCase().contains(filter)
                    || c.getClientVehicle().getLicensePlate().toLowerCase().contains(filter)
                    || (c.getLocation().getAddress() + ", " + c.getLocation().getCity()).toLowerCase().contains(filter)
                    || (c.getFirstOperator().getFirstName() + " " + c.getFirstOperator().getLastName()).toLowerCase().contains(filter)
                    || (c.getLastEditedOperator().getFirstName() + " " + c.getLastEditedOperator().getLastName()).toLowerCase().contains(filter)){
                results.add(c);
            }
        }
        return results;
    }

    private List<Case> matchColumn(List<Case> cases, String filter, String column) {
        List<Case> results = new ArrayList<>();
        java.util.function.Function<Case, String> columnExtractor;

        switch (column) {
            case "Case ID" -> columnExtractor = c -> String.valueOf(c.getId());
            case "Last name" -> columnExtractor = c -> c.getClient().getLastName();
            case "First name" -> columnExtractor = c -> c.getClient().getFirstName();
            case "Vehicle brand" -> columnExtractor = c -> c.getClientVehicle().getModel().toString();
            case "License plate" -> columnExtractor = c -> c.getClientVehicle().getLicensePlate();
            case "Location" -> columnExtractor = c -> c.getLocation().getAddress() + ", " + c.getLocation().getCity();
            case "First edited operator" -> columnExtractor = c -> c.getFirstOperator().getFirstName() + " " + c.getFirstOperator().getLastName();
            case "Last edited operator" -> columnExtractor = c -> c.getLastEditedOperator().getFirstName() + " " + c.getLastEditedOperator().getLastName();
            default -> columnExtractor = c -> "";
        }

        for (Case c : cases) {
            String value = columnExtractor.apply(c);
            if (value != null && value.toLowerCase().contains(filter)) {
                results.add(c);
            }
        }

        return results;
    }
}
