package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.enums.*;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.RepositoryHelper;
import com.davidskopljak.skopljakzavrsni.repository.DatabaseConnectionManager;
import com.davidskopljak.skopljakzavrsni.repository.DriverRepository;
import com.davidskopljak.skopljakzavrsni.repository.WorkshopRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class ServiceController{
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private ComboBox<ServiceType> serviceTypeComboBox;

    @FXML
    private ComboBox<Workshop> workshopComboBox;

    @FXML
    private TextField driverNoteTextField;

    @FXML
    private ComboBox<Driver> driverComboBox;

    @FXML
    private Button confirmButton;
    private Stage stage;
    private Service activeService;
    private Long caseId;

    public void initialize() {
        initializeBaseData();
    }

    private void initializeActiveService() {
        if (activeService != null) {
            driverComboBox.getItems().stream()
                    .filter(d -> d.getId().equals(activeService.getAssignedDriver().getId()))
                    .findFirst()
                    .ifPresent(d -> driverComboBox.getSelectionModel().select(d));

            serviceTypeComboBox.getItems().stream()
                    .filter(t -> t.equals(activeService.getServiceType()))
                    .findFirst()
                    .ifPresent(t -> serviceTypeComboBox.getSelectionModel().select(t));

            workshopComboBox.getItems().stream()
                    .filter(w -> w.getId().equals(activeService.getWorkshop().getId()))
                    .findFirst()
                    .ifPresent(w -> workshopComboBox.getSelectionModel().select(w));
            refreshEditableState();
        }

        // this is here instead of in initialize() because the menu can't refresh it's service state properly if the active service isn't set yet
        injectServiceMenuController();
    }

    private void initializeBaseData() {
        loadServiceTypes();
        loadDrivers();
        loadWorkshops();
    }

    private void loadServiceTypes() {
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection()) {
            var serviceTypes = RepositoryHelper.queryAllServiceTypes(conn);
            ObservableList<ServiceType> typeList = FXCollections.observableArrayList(serviceTypes);
            serviceTypeComboBox.setItems(typeList);
        } catch (RepositoryAccessException e) {
            throw new RepositoryAccessException("Failed to load service types from database: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RepositoryAccessException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    private void loadDrivers() {
        try {
            DriverRepository driverRepository = new DriverRepository();
            var drivers = driverRepository.findAll();
            ObservableList<Driver> driverList = FXCollections.observableArrayList(drivers);
            driverComboBox.setItems(driverList);

            setupDriverComboBox(driverComboBox);
            setupWorkshopComboBox(workshopComboBox);

        } catch (RepositoryAccessException e) {
            CRMApplication.log.error("Failed to load drivers from database", e);
        }
    }

    private void loadWorkshops() {
        try {
            WorkshopRepository workshopRepository = new WorkshopRepository();
            var workshops = workshopRepository.findAll();
            ObservableList<Workshop> workshopList = FXCollections.observableArrayList(workshops);
            workshopComboBox.setItems(workshopList);
        } catch (RepositoryAccessException e) {
            throw new RepositoryAccessException("Failed to load workshops from database: " + e.getMessage(), e);
        }
    }

    private void setupDriverComboBox(ComboBox<Driver> comboBox) {
        Function<Driver, String> formatDriver = d -> d == null ? "" : d.getFirstName() + " " + d.getLastName();

        comboBox.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Driver d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty ? "" : formatDriver.apply(d));
            }
        });

        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Driver d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty ? "" : formatDriver.apply(d));
            }
        });
    }

    private void setupWorkshopComboBox(ComboBox<Workshop> comboBox) {
        Function<Workshop, String> formatWorkshop = w -> w == null ? "" : w.getName();

        comboBox.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Workshop w, boolean empty) {
                super.updateItem(w, empty);
                setText(empty ? "" : formatWorkshop.apply(w));
            }
        });

        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Workshop w, boolean empty) {
                super.updateItem(w, empty);
                setText(empty ? "" : formatWorkshop.apply(w));
            }
        });
    }

    private void injectServiceMenuController() {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("service-menu.fxml"));
            Parent menuRoot = loader.load();
            ServiceMenuController serviceMenuController = loader.getController();
            serviceMenuController.setServiceController(this);
            serviceMenuController.setCaseId(this.caseId);
            rootAnchorPane.getChildren().addFirst(menuRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshEditableState() {
        if (activeService == null) return;

        boolean editable = activeService.getState() == ServiceState.ASSIGNED
                || activeService.getState() == ServiceState.IN_PROGRESS;

        workshopComboBox.setDisable(!editable);
        serviceTypeComboBox.setDisable(!editable);
        driverComboBox.setDisable(!editable);
        driverNoteTextField.setDisable(!editable);
        confirmButton.setDisable(!editable);
    }

    public void setActiveService(Service service) {
        this.activeService = service;
        initializeActiveService();
        refreshEditableState();
    }

    public Service getActiveService() {
        return activeService;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }


    public void setServiceInfo() {
        if (this.activeService == null) {
            validateNewServiceInputs();

            this.activeService = new Service(
                    caseId,
                    driverComboBox.getValue(),
                    serviceTypeComboBox.getValue(),
                    workshopComboBox.getValue(),
                    ServiceState.ASSIGNED,
                    getDriverNotes()
            );

        } else {
            validateExistingService();
            activeService.setDriverNotes(getDriverNotes());
        }
    }

    private void validateNewServiceInputs() {
        if (workshopComboBox.getSelectionModel().isEmpty()) {
            throw new IllegalStateException("Workshop must be selected");
        }

        if (serviceTypeComboBox.getSelectionModel().isEmpty()) {
            throw new IllegalStateException("Service type must be selected");
        }

        if (driverComboBox.getSelectionModel().isEmpty() ||
                !DriverState.AVAILABLE.equals(driverComboBox.getSelectionModel().getSelectedItem().getState())) {
            throw new IllegalStateException("Available driver must be selected");
        }
    }

    private void validateExistingService() {
        boolean changed = !workshopComboBox.getValue().getId().equals(activeService.getWorkshop().getId())
                || !driverComboBox.getValue().getId().equals(activeService.getAssignedDriver().getId())
                || !serviceTypeComboBox.getValue().equals(activeService.getServiceType());

        if (changed) {
            throw new IllegalStateException("Service information cannot be changed after service has been assigned");
        }
    }


    private String getDriverNotes() {
        return driverNoteTextField.getText().isEmpty() ? "" : driverNoteTextField.getText();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }
}
