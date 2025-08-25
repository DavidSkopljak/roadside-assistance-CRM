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
import javafx.scene.Scene;
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

    private Stage stage;
    private Service activeService;
    private ServiceMenuController serviceMenuController;
    private ServicesListController servicesListController;
    private Long caseId;

    public void initialize() {
        injectServiceMenuController();
        initializeBaseData();
        initializeActiveService();
    }

    private void initializeActiveService() {
        if (activeService == null) return;

        if (activeService.getAssignedDriver() != null) {
            driverComboBox.getSelectionModel().select(activeService.getAssignedDriver());
        }
        if (activeService.getServiceType() != null) {
            serviceTypeComboBox.getSelectionModel().select(activeService.getServiceType());
        }
        if (activeService.getWorkshop() != null) {
            workshopComboBox.getSelectionModel().select(activeService.getWorkshop());
        }
    }

    private void initializeBaseData() {
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();){
            var serviceTypes = RepositoryHelper.queryAllServiceTypes(conn);
            ObservableList<ServiceType> typeList = FXCollections.observableArrayList(serviceTypes);
            serviceTypeComboBox.setItems(typeList);
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException("Failed to load service types from database: " + e.getMessage());
        }catch(SQLException e){
            throw new RepositoryAccessException("Failed to connect to database: " + e.getMessage());
        }

        try{
            DriverRepository driverRepository = new DriverRepository();
            var drivers = driverRepository.findAll();
            ObservableList<Driver> driverList = FXCollections.observableArrayList(drivers);
            driverComboBox.setItems(driverList);

            Function<Driver, String> formatDriver = d -> d == null ? "" : d.getFirstName() + " " + d.getLastName();

            driverComboBox.setCellFactory(cb -> new ListCell<>() {
                @Override
                protected void updateItem(Driver d, boolean empty) {
                    super.updateItem(d, empty);
                    setText(empty ? "" : formatDriver.apply(d));
                }
            });
            driverComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Driver d, boolean empty) {
                    super.updateItem(d, empty);
                    setText(empty ? "" : formatDriver.apply(d));
                }
            });

            Function<Workshop, String> formatWorkshop = w -> w == null ? "" : w.getName() ;

            workshopComboBox.setCellFactory(cb -> new ListCell<>() {
                @Override
                protected void updateItem(Workshop w, boolean empty) {
                    super.updateItem(w, empty);
                    setText(empty ? "" : formatWorkshop.apply(w));
                }
            });
            workshopComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Workshop w, boolean empty) {
                    super.updateItem(w, empty);
                    setText(empty ? "" : formatWorkshop.apply(w));
                }
            });


        }catch (SQLException e){
            throw new RepositoryAccessException("Failed to load drivers from database: " + e.getMessage());
        }

        try{
            WorkshopRepository workshopRepository = new WorkshopRepository();
            var workshops = workshopRepository.findAll();
            ObservableList<Workshop> workshopList = FXCollections.observableArrayList(workshops);
            workshopComboBox.setItems(workshopList);
        }catch(SQLException e){
            throw new RepositoryAccessException("Failed to load workshops from database: " + e.getMessage());
        }

    }

    private void injectServiceMenuController() {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("service-menu.fxml"));
            Parent menuRoot = loader.load();
            this.serviceMenuController = loader.getController();
            this.serviceMenuController.setServiceController(this);
            rootAnchorPane.getChildren().addFirst(menuRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServicesListController(ServicesListController servicesListController) {
        this.servicesListController = servicesListController;
    }

    public void setActiveService(Service service) {
        this.activeService = service;
        initializeActiveService();
    }

    public Service getActiveService() {
        return activeService;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Long getCaseId() {
        return this.caseId;
    }

    public void setServiceInfo(){
        if(this.activeService == null){
            if(workshopComboBox.getSelectionModel().isEmpty()){
                throw new IllegalStateException("Workshop must be selected");
            }

            if(serviceTypeComboBox.getSelectionModel().isEmpty()){
                throw new IllegalStateException("Service type must be selected");
            }

            if(driverComboBox.getSelectionModel().isEmpty() && driverComboBox.getSelectionModel().getSelectedItem().getState() == DriverState.AVAILABLE){
                throw new IllegalStateException("Available driver must be selected");
            }
            this.activeService = new Service(
                    caseId,
                    driverComboBox.getValue(),
                    serviceTypeComboBox.getValue(),
                    ServiceState.ASSIGNED,
                    driverNoteTextField.getText().isEmpty() ? "" : driverNoteTextField.getText()
            );
        }
        else{
            if(workshopComboBox.getValue() != activeService.getWorkshop()
                    || driverComboBox.getValue() != activeService.getAssignedDriver()
                    || serviceTypeComboBox.getValue() != activeService.getServiceType()){
                throw new IllegalStateException("Service information cannot be changed after service has been assigned");
            }else{
                activeService.setDriverNotes(driverNoteTextField.getText().isEmpty() ? "" : driverNoteTextField.getText());
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }
}
