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
    private String driverNotes;

    public Service(Long id, Long caseId, Driver assignedDriver, ServiceType serviceType, Workshop workshop, ServiceState serviceState, String driverNotes){
        super(id);
        this.caseId = caseId;
        this.assignedDriver = assignedDriver;
        this.serviceType = serviceType;
        this.serviceState = serviceState;
        this.driverNotes = driverNotes;
        if(this.serviceType == ServiceType.TOWING){
            this.workshop = workshop;
        }else{
            throw new IllegalArgumentException("Service type must be towing for workshop to be set");
        }
    }

    public Service(Long caseId, Driver assignedDriver, ServiceType serviceType, Workshop workshop, ServiceState serviceState, String driverNotes) {
        this.caseId = caseId;
        this.assignedDriver = assignedDriver;
        this.serviceType = serviceType;
        this.serviceState = serviceState;
        this.driverNotes = driverNotes;
        if(this.serviceType == ServiceType.TOWING){
            this.workshop = workshop;
        }else{
            throw new IllegalArgumentException("Service type must be towing for workshop to be set");
        }
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

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getDriverNotes() {
        return driverNotes;
    }

    public void setDriverNotes(String driverNotes) {
        this.driverNotes = driverNotes;
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
