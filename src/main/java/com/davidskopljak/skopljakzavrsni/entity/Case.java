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
    private  Location location;
    private  Operator firstOperator;
    private  Operator lastEditedOperator;
    private  Vehicle clientVehicle;
    private  LocalDate clientVehicleFirstRegistrationDate;
    private  List<Note> caseNotes;
    private  String damageDescription;
    private  Optional<Service> activeService;
    private  CaseState caseState;
    private  VehicleDamageType damageType;
    private  VehicleDamageCause damageCause;
    private  LocalDateTime createdDateTime;
    private  Client client;

    private Case(Builder builder) {
        super(builder.id);
        this.location = builder.location;
        this.firstOperator = builder.firstOperator;
        this.lastEditedOperator = builder.lastEditedOperator;
        this.clientVehicle = builder.clientVehicle;
        this.clientVehicleFirstRegistrationDate = builder.clientVehicleFirstRegistrationDate;
        this.caseNotes = builder.caseNotes;
        this.damageDescription = builder.damageDescription;
        this.activeService = builder.activeService;
        this.caseState = builder.caseState;
        this.damageType = builder.damageType;
        this.damageCause = builder.damageCause;
        this.createdDateTime = builder.createdDateTime;
        this.client = builder.client;
    }

    public Location getLocation() {
        return location;
    }

    public Operator getFirstOperator() {
        return firstOperator;
    }

    public Operator getLastEditedOperator() {
        return lastEditedOperator;
    }

    public Vehicle getClientVehicle() {
        return clientVehicle;
    }

    public LocalDate getClientVehicleFirstRegistrationDate() {
        return clientVehicleFirstRegistrationDate;
    }

    public List<Note> getCaseNotes() {
        return caseNotes;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public Optional<Service> getActiveService() {
        return activeService;
    }

    public CaseState getCaseState() {
        return caseState;
    }

    public VehicleDamageType getDamageType() {
        return damageType;
    }

    public VehicleDamageCause getDamageCause() {
        return damageCause;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public Client getClient() {
        return client;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLastEditedOperator(Operator lastEditedOperator) {
        this.lastEditedOperator = lastEditedOperator;
    }

    public void setFirstOperator(Operator firstOperator) {
        this.firstOperator = firstOperator;
    }

    public void setClientVehicle(Vehicle clientVehicle) {
        this.clientVehicle = clientVehicle;
    }

    public void setClientVehicleFirstRegistrationDate(LocalDate clientVehicleFirstRegistrationDate) {
        this.clientVehicleFirstRegistrationDate = clientVehicleFirstRegistrationDate;
    }

    public void setCaseNotes(List<Note> caseNotes) {
        this.caseNotes = caseNotes;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public void setActiveService(Optional<Service> activeService) {
        this.activeService = activeService;
    }

    public void setCaseState(CaseState caseState) {
        this.caseState = caseState;
    }

    public void setDamageType(VehicleDamageType damageType) {
        this.damageType = damageType;
    }

    public void setDamageCause(VehicleDamageCause damageCause) {
        this.damageCause = damageCause;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public CaseState getState() {
        return caseState;
    }

    @Override
    public void updateState(CaseState state) {
        this.caseState = state;
    }

    @Override
    public List<Note> getNotes() {
        return caseNotes;
    }

    @Override
    public void addNotes(List<Note> notes) {
        caseNotes.addAll(notes);
    }

    @Override
    public void addNote(Note note) {
        caseNotes.add(note);
    }

    public static class Builder {
        private Long id;
        private Location location;
        private Operator firstOperator;
        private Operator lastEditedOperator;
        private Vehicle clientVehicle;
        private LocalDate clientVehicleFirstRegistrationDate;
        private List<Note> caseNotes = new ArrayList<>();
        private String damageDescription;
        private Optional<Service> activeService = Optional.empty();
        private CaseState caseState;
        private VehicleDamageType damageType;
        private VehicleDamageCause damageCause;
        private LocalDateTime createdDateTime;
        private Client client;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder firstOperator(Operator firstOperator) {
            this.firstOperator = firstOperator;
            return this;
        }

        public Builder lastEditedOperator(Operator lastEditedOperator) {
            this.lastEditedOperator = lastEditedOperator;
            return this;
        }

        public Builder clientVehicle(Vehicle clientVehicle) {
            this.clientVehicle = clientVehicle;
            return this;
        }

        public Builder clientVehicleFirstRegistrationDate(LocalDate date) {
            this.clientVehicleFirstRegistrationDate = date;
            return this;
        }

        public Builder notes(List<Note> notes) {
            this.caseNotes = new ArrayList<>(notes);
            return this;
        }

        public Builder damageDescription(String description) {
            this.damageDescription = description;
            return this;
        }

        public Builder activeService(Optional<Service> service) {
            this.activeService = service;
            return this;
        }

        public Builder caseState(CaseState state) {
            this.caseState = state;
            return this;
        }

        public Builder damageType(VehicleDamageType type) {
            this.damageType = type;
            return this;
        }

        public Builder damageCause(VehicleDamageCause cause) {
            this.damageCause = cause;
            return this;
        }

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public Builder createdDateTime(LocalDateTime dateTime) {
            this.createdDateTime = dateTime;
            return this;
        }

        public Case build() {
            return new Case(this);
        }
    }

}
