package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageCause;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageType;
import com.davidskopljak.skopljakzavrsni.interfaces.Noteable;
import com.davidskopljak.skopljakzavrsni.interfaces.Trackable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public non-sealed class Case extends Entity implements Trackable<CaseState>, Noteable {
    private Location location;
    private Operator firstOperator;
    private Operator lastEditedOperator;
    private Vehicle clientVehicle;
    private LocalDate clientVehicleFirstRegistrationDate;
    private List<Note> caseNotes = new ArrayList<>();
    private String damageDescription;
    private Optional<Service> activeService = Optional.empty();

    public void setCaseState(CaseState caseState) {
        this.caseState = caseState;
    }

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
    public void addNotes(List<Note> notes) {
        caseNotes.addAll(notes);
    }

    public void addNote(Note note) {
        this.caseNotes.add(note);
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

    public VehicleDamageType getDamageType() {
        return damageType;
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

    public LocalDate getClientVehicleFirstRegistrationDate() {
        return clientVehicleFirstRegistrationDate;
    }

    public Case setClientVehicleFirstRegistrationDate(LocalDate clientVehicleFirstRegistrationDate) {
        this.clientVehicleFirstRegistrationDate = clientVehicleFirstRegistrationDate;
        return this;
    }

}
