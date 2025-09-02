package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.enums.ServiceState;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.repository.ServiceRepository;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.sql.SQLException;

public class ServiceMenuController {
    @FXML
    MenuItem resolveServiceMenuItem;
    @FXML
    MenuItem cancelServiceMenuItem;
    @FXML
    MenuItem reactivateServiceMenuItem;

    private ServiceController serviceController;

    public void handleSaveService() {
        try{
            ServiceRepository serviceRepository = new ServiceRepository();
            if(serviceController.getActiveService().getId() != null) {
                serviceRepository.update(serviceController.getActiveService());
            }else {
                Long serviceId = serviceRepository.save(serviceController.getActiveService());
                serviceController.getActiveService().setId(serviceId);
            }
            serviceController.refreshEditableState();
            this.refreshEditableState();
        }catch(SQLException e){
            throw new RepositoryAccessException("Failed to save service: " + e);
        }
    }

    public void handleViewServiceNotes() { /*TODO add logic to open service notes*/}

    public void handleResolveService() {
        if(serviceController.getActiveService().getId() == null){
            throw new IllegalStateException("Service must be saved before resolving it");
        }else{
            serviceController.getActiveService().setServiceState(ServiceState.FINISHED);
            handleSaveService();
        }
    }

    public void handleCancelService() {
        if(serviceController.getActiveService().getId() == null){
            throw new IllegalStateException("Service must be saved before resolving it");
        }else{
            serviceController.getActiveService().setServiceState(ServiceState.CANCELLED);
            handleSaveService();
        }
    }

    public void handleReactivateService(){
        if(serviceController.getActiveService().getId() == null){
            throw new IllegalStateException("Service must be saved before reactivating it");
        }else{
            serviceController.getActiveService().setServiceState(ServiceState.IN_PROGRESS);
            handleSaveService();
        }
    }

    public void handleExitService() {
        serviceController.getStage().close();
    }

    public void setServiceController(ServiceController controller) {
        this.serviceController = controller;
        if(controller.getActiveService() != null){
            this.refreshEditableState();
        }
    }

    public void setCaseId(Long caseId) {
        this.serviceController.setCaseId(caseId);
    }

    public void refreshEditableState(){
        if(serviceController.getActiveService() == null){
            reactivateServiceMenuItem.setDisable(true);
            resolveServiceMenuItem.setDisable(true);
            cancelServiceMenuItem.setDisable(true);
        } else if(serviceController.getActiveService().getServiceState() == ServiceState.FINISHED
                || serviceController.getActiveService().getServiceState() == ServiceState.CANCELLED ){
            reactivateServiceMenuItem.setDisable(false);
            resolveServiceMenuItem.setDisable(true);
            cancelServiceMenuItem.setDisable(true);
        } else if(serviceController.getActiveService().getServiceState() == ServiceState.IN_PROGRESS
            || serviceController.getActiveService().getServiceState() == ServiceState.ASSIGNED){
            reactivateServiceMenuItem.setDisable(true);
            resolveServiceMenuItem.setDisable(false);
            cancelServiceMenuItem.setDisable(false);
        }
    }
}
