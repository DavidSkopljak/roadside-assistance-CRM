package com.davidskopljak.skopljakzavrsni.controller;

public class ServiceMenuController {
    private ServiceController serviceController;

    public void handleSaveService() {}
    public void handleViewServiceNotes() {}
    public void handleResolveService() {}
    public void handleCancelService() {}
    public void setServiceController(ServiceController controller) {
        this.serviceController = controller;
    }
}
