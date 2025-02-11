package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.ServiceState;
import com.davidskopljak.skopljakzavrsni.enums.ServiceType;
import com.davidskopljak.skopljakzavrsni.interfaces.Noteable;
import com.davidskopljak.skopljakzavrsni.interfaces.Trackable;

import java.util.ArrayList;
import java.util.List;

public non-sealed class Service extends Entity implements Trackable<ServiceState>, Noteable {
    private Driver assignedDriver;
    private ServiceType serviceType;
    private ServiceState serviceState;
    private List<String> serviceNotes = new ArrayList<>();

    public Service(Long id, Driver assignedDriver, ServiceType serviceType){
        super(id);
        this.assignedDriver = assignedDriver;
        this.serviceType = serviceType;
        this.serviceState = ServiceState.ASSIGNED;
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public ServiceState getServiceState() {
        return serviceState;
    }

    @Override
    public ServiceState getState() {
        return serviceState;
    }

    @Override
    public void updateState(ServiceState state) {
        serviceState = state;
    }

    @Override
    public List<String> getNotes() {
        return serviceNotes;
    }

    @Override
    public void setNotes(List<String> notes) {
        serviceNotes.addAll(notes);
    }
}
