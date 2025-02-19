package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.controller.CRMApplication;
import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageCause;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageType;
import com.davidskopljak.skopljakzavrsni.helpers.JSONParser;
import com.davidskopljak.skopljakzavrsni.interfaces.Noteable;
import com.davidskopljak.skopljakzavrsni.interfaces.Trackable;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private Optional<Service> activeService;
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
        try{
            caseNotes = JSONParser.parseCaseNotes(notes);
            System.out.println("Case notes in Case.setNotes() - " + caseNotes.toString());
        }catch (SQLException e){
            System.out.println("SQLException in Case.setNotes() - " + e.getMessage());
            CRMApplication.log.error(e.getMessage());
        }
    }

    public Location getLocation() {
        return location;
    }

    public Case setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Operator getFirstOperator() {
        return firstOperator;
    }

    public Case setFirstOperator(Operator firstOperator) {
        this.firstOperator = firstOperator;
        return this;
    }

    public Operator getLastEditedOperator() {
        return lastEditedOperator;
    }

    public Case setLastEditedOperator(Operator lastEditedOperator) {
        this.lastEditedOperator = lastEditedOperator;
        return this;
    }

    public Vehicle getClientVehicle() {
        return clientVehicle;

    }

    public Case setClientVehicle(Vehicle clientVehicle) {
        this.clientVehicle = clientVehicle;
        return this;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public Case setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
        return this;
    }

    public Optional<Service> getActiveService() {
        return activeService;
    }

    public Case setActiveService(Optional<Service> activeService) {
        this.activeService = activeService;
        return this;
    }

    public Case setDamageType(VehicleDamageType damageType) {
        this.damageType = damageType;
        return this;
    }

    public VehicleDamageCause getDamageCause() {
        return damageCause;
    }

    public Case setDamageCause(VehicleDamageCause damageCause) {
        this.damageCause = damageCause;
        return this;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public Case setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public Case setClient(Client client) {
        this.client = client;
        return this;
    }

    public VehicleDamageType getDamageType() {
        return damageType;
    }
}
