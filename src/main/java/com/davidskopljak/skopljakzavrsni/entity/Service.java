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
    private Workshop workshop;
    private Long caseId;

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

    public Service(Long id, Service service) {
        super(id);
        this.assignedDriver = service.getAssignedDriver();
        this.serviceType = service.getServiceType();
        this.serviceState = service.getState();
        this.serviceNotes = service.getNotes();
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

    public ServiceState getServiceState() {
        return serviceState;
    }

    public List<Note> getServiceNotes() {
        return serviceNotes;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public void setAssignedDriver(Driver assignedDriver) {
        this.assignedDriver = assignedDriver;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setServiceState(ServiceState serviceState) {
        this.serviceState = serviceState;
    }

    public void setServiceNotes(List<Note> serviceNotes) {
        this.serviceNotes = serviceNotes;
    }

    public void setWorkshop(Workshop workshop) {
        if(this.serviceType == ServiceType.TOWING){
            this.workshop = workshop;
        }else{
            throw new IllegalArgumentException("Service type must be towing for workshop to be set");
        }
    }

    public Long getCaseId() {
        return caseId;
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
    public void addNotes(List<Note> notes) {
        serviceNotes.addAll(notes);
    }

    @Override
    public void addNote(Note note) {
        serviceNotes.add(note);
    }
}
