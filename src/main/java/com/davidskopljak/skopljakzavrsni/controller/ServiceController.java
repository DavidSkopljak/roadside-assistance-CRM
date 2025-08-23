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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
    private ServiceMenuController serviceMenuController;
    private ServicesListController servicesListController;
    private Long caseId;

    public void initialize() {
        injectServiceMenuController();
        initializeActiveService();
    }

    public void setActiveService(Service service) {
        this.activeService = service;
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

    private void initializeBaseData() throws SQLException {
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();){
            var serviceTypes = RepositoryHelper.queryAllServiceTypes(conn);
            ObservableList<ServiceType> typeList = FXCollections.observableArrayList(serviceTypes);
            serviceTypeComboBox.setItems(typeList);
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException("Failed to load service types from database: " + e.getMessage());
        }

        DriverRepository driverRepository = new DriverRepository();
        var drivers = driverRepository.findAll();
        ObservableList<Driver> driverList = FXCollections.observableArrayList(drivers);
        driverComboBox.setItems(driverList);

        WorkshopRepository workshopRepository = new WorkshopRepository();
        var workshops = workshopRepository.findAll();
        ObservableList<Workshop> workshopList = FXCollections.observableArrayList(workshops);
        workshopComboBox.setItems(workshopList);
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

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public void setServiceInfo(){

    }
}
