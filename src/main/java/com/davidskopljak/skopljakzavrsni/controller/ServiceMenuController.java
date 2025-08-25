package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.enums.ServiceState;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.repository.ServiceRepository;

import java.sql.SQLException;

public class ServiceMenuController {
    private ServiceController serviceController;

    public void handleSaveService() {
        try{
            ServiceRepository serviceRepository = new ServiceRepository();
            if(serviceController.getActiveService().getId() != null) {
                serviceRepository.update(serviceController.getActiveService());
            }else{
                serviceRepository.save(serviceController.getActiveService());
            }
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
        }
    }

    public void handleCancelService() {
        if(serviceController.getActiveService().getId() == null){
            throw new IllegalStateException("Service must be saved before resolving it");
        }else{
            serviceController.getActiveService().setServiceState(ServiceState.CANCELLED);
        }
    }

    public void handleExitService() {
        serviceController.getStage().close();
    }

    public void setServiceController(ServiceController controller) {
        this.serviceController = controller;
    }
}
