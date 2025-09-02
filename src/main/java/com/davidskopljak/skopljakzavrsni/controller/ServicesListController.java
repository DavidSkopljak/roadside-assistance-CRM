package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Service;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.interfaces.CaseController;
import com.davidskopljak.skopljakzavrsni.repository.ServiceRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ServicesListController implements CaseController {
    private CaseWindowController caseWindowController;
    private ServicesListMenuController servicesListMenuController;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TableView<Service> servicesTableView;

    @FXML
    private TableColumn<Service, Long> serviceIdTableColumn;

    @FXML
    private TableColumn<Service, String> serviceTypeTableColumn;

    @FXML
    private TableColumn<Service, String> driverLastNameTableColumn;

    @FXML
    private TableColumn<Service, String> driverFirstNameTableColumn;

    public void initialize() {
        initializeServicesTableView();
        injectServicesListMenu();
    }

    public void setCaseWindowController(CaseWindowController caseWindowController){
        this.caseWindowController = caseWindowController;
        try{
            ServiceRepository serviceRepository = new ServiceRepository();
            servicesTableView.getItems().addAll(serviceRepository.findAllByCaseId(this.caseWindowController.getActiveCase().getId()));
            servicesListMenuController.refreshEditableState();
        } catch (Exception e) {
            throw new RepositoryAccessException("Failed to load services: " + e);
        }
    }

    private void injectServicesListMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("services-list-menu.fxml"));
            Parent menuRoot = loader.load();
            servicesListMenuController = loader.getController();
            servicesListMenuController.setServicesListController(this);
            rootAnchorPane.getChildren().addFirst(menuRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeServicesTableView() {
        serviceIdTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleLongProperty(cellData.getValue().getId()).asObject());

        serviceTypeTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getServiceType().toString()));

        driverLastNameTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAssignedDriver().getFirstName()));

        driverFirstNameTableColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAssignedDriver().getLastName()));

        servicesTableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Service selectedService = servicesTableView.getSelectionModel().getSelectedItem();
                if (selectedService != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("service.fxml"));
                        Parent root = loader.load();
                        ServiceController serviceController = loader.getController();
                        Long caseId = this.caseWindowController.getActiveCase().getId();
                        if(caseId != null) {
                            serviceController.setCaseId(caseId);
                        }
                        serviceController.setActiveService(selectedService);
                        Stage stage = new Stage();
                        serviceController.setStage(stage);
                        stage.setScene(new Scene(root));
                        stage.setTitle("New service");
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public CaseWindowController getCaseWindowController() {
        return this.caseWindowController;
    }
}
