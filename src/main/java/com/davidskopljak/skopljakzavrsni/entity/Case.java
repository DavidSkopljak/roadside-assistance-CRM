package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageType;
import com.davidskopljak.skopljakzavrsni.interfaces.Noteable;
import com.davidskopljak.skopljakzavrsni.interfaces.Trackable;

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
    private List<String> caseNotes = new ArrayList<>();
    private String damageDescription;
    private List<Service> services = new ArrayList<>();
    private CaseState caseState;
    private VehicleDamageType damageType;

    public Case(Long id, Location location, Operator firstOperator, Vehicle clientVehicle, String damageDescription, VehicleDamageType damageType) {
        super(id);
        this.location = location;
        this.firstOperator = firstOperator;
        this.lastEditedOperator = firstOperator;
        this.clientVehicle = clientVehicle;
        this.damageDescription = damageDescription;
        this.caseState = CaseState.ACTIVE;
        this.damageType = damageType;
    }

    public Case(Location location, Operator firstOperator, Vehicle clientVehicle, String damageDescription, VehicleDamageType damageType) {
        this.location = location;
        this.firstOperator = firstOperator;
        this.lastEditedOperator = firstOperator;
        this.clientVehicle = clientVehicle;
        this.damageDescription = damageDescription;
        this.caseState = CaseState.ACTIVE;
        this.damageType = damageType;
    }

    public VehicleDamageType getDamageType() {
        return damageType;
    }

    public void setLastEditedOperator(Operator lastEditedOperator) {
        this.lastEditedOperator = lastEditedOperator;
    }

    @Override
    public CaseState getState() {
        return caseState;
    }

    @Override
    public void updateState(CaseState state) {
        caseState = state;
    }

    @Override
    public List<String> getNotes() {
        return caseNotes;
    }

    @Override
    public void setNotes(List<String> notes) {
        caseNotes.addAll(notes);
    }

    public class Builder{
        private Long id;
        private Location location;
        private Operator firstOperator;
        private Vehicle clientVehicle;
        private String damageDescription;
        private VehicleDamageType damageType;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }
        public Builder setLocation(Location location) {
            this.location = location;
            return this;
        }
        public Builder setFirstOperator(Operator firstOperator) {
            this.firstOperator = firstOperator;
            return this;
        }
        public Builder setClientVehicle(Vehicle clientVehicle) {
            this.clientVehicle = clientVehicle;
            return this;
        }
        public Builder setDamageDescription(String damageDescription) {
            this.damageDescription = damageDescription;
            return this;
        }

        public Builder setDamageType (VehicleDamageType damageType) {
            this.damageType = damageType;
            return this;
        }

        public Case build() {
            return new Case(id, location, firstOperator, clientVehicle, damageDescription, damageType);
        }
    }

    public List<Service> getServices() {
        return services;
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

    public String getDamageDescription() {
        return damageDescription;
    }
}
