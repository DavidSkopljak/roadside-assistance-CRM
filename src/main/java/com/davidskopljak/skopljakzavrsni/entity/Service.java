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
    private List<Note> serviceNotes = new ArrayList<>();

    public Service(Long id, Driver assignedDriver, ServiceType serviceType, ServiceState serviceState){
        super(id);
        this.assignedDriver = assignedDriver;
        this.serviceType = serviceType;
        this.serviceState = serviceState;
    }

    public Service(Driver assignedDriver, ServiceType serviceType, ServiceState serviceState) {
        this.assignedDriver = assignedDriver;
        this.serviceType = serviceType;
        this.serviceState = serviceState;
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    public ServiceType getServiceType() {
        return serviceType;
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
    public List<Note> getNotes() {
        return serviceNotes;
    }

    @Override
    public void setNotes(String notes) {
        //serviceNotes.addAll(notes);
        //process json data from db and turn into Note objects
    }
}
