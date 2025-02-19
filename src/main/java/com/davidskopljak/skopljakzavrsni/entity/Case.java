package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageCause;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageType;
import com.davidskopljak.skopljakzavrsni.interfaces.Noteable;
import com.davidskopljak.skopljakzavrsni.interfaces.Trackable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// case, needs info about client, their vehicle, total number of people who need assistance,
// location of case, ability to assign driver, ability to add notes, cause of accident/vehicle damage, nature and description of
// vehicle damage, ability to assign state of case(no driver assigned, driver en route, cancelled, resolved)

//this class has required fields(Long id, Location location, Driver assignedDriver, Operator firstOperator, Vehicle clientVehicle, String damageDescription, CauseOfVehicleDamage causeOfDamage, VehicleDamageType damageType) and optional ones (caseNotes, lastEditedOperator)
public non-sealed class Case extends Entity implements Trackable<CaseState>, Noteable {
    private Location location;
    private Operator firstOperator;
    private Operator lastEditedOperator;
    private Vehicle clientVehicle;
    private List<Note> caseNotes = new ArrayList<>();
    private String damageDescription;
    private List<Service> services = new ArrayList<>();
    private CaseState caseState;
    private VehicleDamageType damageType;
    private VehicleDamageCause damageCause;
    private LocalDateTime createdDateTime;
    private Client client;

    public Case(Long id) {
        super(id);
    }

    public Case(){}

    @Override
    public CaseState getState() {
        return caseState;
    }

    @Override
    public void updateState(CaseState state) {
        caseState = state;
    }

    @Override
    public List<Note> getNotes() {
        return caseNotes;
    }

    @Override
    public void setNotes(String notes) {
        //caseNotes.addAll(notes);
        //process json and turn into Note objects, store in notes field
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Operator getFirstOperator() {
        return firstOperator;
    }

    public void setFirstOperator(Operator firstOperator) {
        this.firstOperator = firstOperator;
    }

    public Operator getLastEditedOperator() {
        return lastEditedOperator;
    }

    public void setLastEditedOperator(Operator lastEditedOperator) {
        this.lastEditedOperator = lastEditedOperator;
    }

    public Vehicle getClientVehicle() {
        return clientVehicle;
    }

    public void setClientVehicle(Vehicle clientVehicle) {
        this.clientVehicle = clientVehicle;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void setDamageType(VehicleDamageType damageType) {
        this.damageType = damageType;
    }

    public VehicleDamageCause getDamageCause() {
        return damageCause;
    }

    public void setDamageCause(VehicleDamageCause damageCause) {
        this.damageCause = damageCause;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public VehicleDamageType getDamageType() {
        return damageType;
    }
}
