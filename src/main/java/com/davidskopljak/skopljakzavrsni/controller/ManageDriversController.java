package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Driver;
import com.davidskopljak.skopljakzavrsni.entity.Person;
import com.davidskopljak.skopljakzavrsni.enums.DriverState;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidDriverInfoException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.repository.DriverRepository;
import com.davidskopljak.skopljakzavrsni.validation.Validators;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class ManageDriversController {
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField contactNumberTextField;

    @FXML
    private TableView<Driver> driversTable;

    @FXML
    private TableColumn<Driver, String> idColumn;

    @FXML
    private TableColumn<Driver, String> firstNameColumn;

    @FXML
    private TableColumn<Driver, String> lastNameColumn;

    @FXML
    private TableColumn<Driver, String> contactNumberColumn;

    @FXML
    private TableColumn<Driver, String> deleteDriverColumn;

    @FXML
    private TextArea filterTextArea;

    @FXML
    private ComboBox<String> filterComboBox;

    private final Image deleteImage = new Image(getClass().getResourceAsStream("/images/x-transparent.png"));

    public void handleAddNewDriver(){
        try{
            validateDriver();
            saveNewDriver();
        } catch(InvalidDriverInfoException e){
            CRMApplication.log.error("Failed to add new driver: ", e);
        }

    }

    public void initialize(){
        filterTextArea.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode() == KeyCode.ENTER) {
                ke.consume();
                MiscHelpers.runThread(() -> {
                    try {
                        DriverRepository driverRepository = new DriverRepository();
                        List<Driver> allDrivers = driverRepository.findAll();
                        final List<Driver> results;

                        String selectedColumn = filterComboBox.getSelectionModel().getSelectedItem();
                        String filter = filterTextArea.getText().trim().toLowerCase();

                        if (selectedColumn == null) {
                            results = matchAnyColumn(allDrivers, filter);
                        } else {
                            results = matchColumn(allDrivers, filter, selectedColumn);
                        }

                        javafx.application.Platform.runLater(() -> driversTable.getItems().setAll(results));
                    } catch (Exception e) {
                        CRMApplication.log.error("Search failed", e);
                    }
                });
            }
        });

        filterComboBox.getItems().addAll(
                "ID",
                "First name",
                "Last name",
                "Contact number"
        );

        setupDriverTable();
    }

    private List<Driver> matchAnyColumn(List<Driver> allDrivers, String filter){
        List<Driver> results = new ArrayList<>();
        for(Driver d : allDrivers){
            if (String.valueOf(d.getId()).contains(filter)
                    || d.getFirstName().toLowerCase().contains(filter)
                    || d.getLastName().toLowerCase().contains(filter)
                    || d.getContactNumber().contains(filter)){
                results.add(d);
            }
        }
        return results;
    }

    private List<Driver> matchColumn(List<Driver> drivers, String filter, String column) {
        List<Driver> results = new ArrayList<>();
        java.util.function.Function<Driver, String> columnExtractor;

        switch (column) {
            case "ID" -> columnExtractor = d -> String.valueOf(d.getId());
            case "Last name" -> columnExtractor = Person::getLastName;
            case "First name" -> columnExtractor = Person::getFirstName;
            case "Contact number" -> columnExtractor = Driver::getContactNumber;
            default -> columnExtractor = d -> "";
        }

        for (Driver d : drivers) {
            String value = columnExtractor.apply(d);
            if (value != null && value.toLowerCase().contains(filter)) {
                results.add(d);
            }
        }

        return results;
    }

    private void setupDriverTable() {
        idColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId().toString()));
        firstNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLastName()));
        contactNumberColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getContactNumber()));

        deleteDriverColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setOnMouseClicked(null);
                } else {
                    ImageView deleteIcon = new ImageView(deleteImage);
                    deleteIcon.setFitWidth(16);
                    deleteIcon.setFitHeight(16);
                    setGraphic(deleteIcon);

                    setOnMouseClicked(event ->
                            handleDeleteDriver(getTableView().getItems().get(getIndex()))
                    );
                }
            }
        });

        refreshDriverTable();
    }

    private void refreshDriverTable() {
        MiscHelpers.runThread(() -> {
            DriverRepository driverRepository = new DriverRepository();
            try {
                List<Driver> drivers = driverRepository.findAll();
                javafx.application.Platform.runLater(() -> driversTable.getItems().setAll(drivers));
            } catch (RepositoryAccessException e) {
                CRMApplication.log.error("Failed to load drivers from database: ", e);
            }
        });
    }

    private void validateDriver() throws InvalidDriverInfoException {
        String firstName = firstNameTextField.getText().trim();
        String lastName = lastNameTextField.getText().trim();
        String contactNumber = contactNumberTextField.getText().trim();

        if(Boolean.FALSE.equals(Validators.isValidString(firstName))
                || Boolean.FALSE.equals(Validators.isValidString(lastName))
                || Boolean.FALSE.equals(Validators.isValidHRPhoneNumber(contactNumber))){
            throw new InvalidDriverInfoException("Invalid driver info.");
        }
    }

    private void saveNewDriver(){
        MiscHelpers.runThread(() -> {
            DriverRepository driverRepository = new DriverRepository();
            try{
                Driver newDriver = new Driver(
                        firstNameTextField.getText().trim(),
                        lastNameTextField.getText().trim(),
                        contactNumberTextField.getText().trim(),
                        DriverState.AVAILABLE
                );
                driverRepository.save(newDriver);
                refreshDriverTable();

                firstNameTextField.clear();
                lastNameTextField.clear();
                contactNumberTextField.clear();

            } catch (RepositoryAccessException e){
                CRMApplication.log.error("Failed to save new driver: ", e);
            }
        });
    }

    private void handleDeleteDriver(Driver driver){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this driver?");
        alert.setContentText(driver.getFirstName() + " " + driver.getLastName());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                MiscHelpers.runThread(() -> {
                    DriverRepository driverRepository = new DriverRepository();
                    try{
                        driverRepository.deleteById(driver.getId());
                        refreshDriverTable();
                    } catch(RepositoryAccessException e){
                        CRMApplication.log.error("Failed to delete driver", e);
                        MiscHelpers.showAlert("Failed to delete driver: " + e.getMessage(), Alert.AlertType.ERROR);
                    }
                });
            }
        });
    }
}