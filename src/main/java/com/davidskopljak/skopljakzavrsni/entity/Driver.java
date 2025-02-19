package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.DriverState;
import com.davidskopljak.skopljakzavrsni.interfaces.Trackable;

// tow truck drivers
// needs to have lists of available equipment and quantity of available equipment
// e.g. crane, rollers, battery starter kit, ...
// also needs to have status field - available, en route to case(new case can be queued), unavailable
// needs current location - this can update every X minutes
// needs field for current vehicle
public class Driver extends Person implements Trackable<DriverState> {
    private String contactNumber;
    private Location currentLocation;
    private Vehicle vehicle;
    private DriverState driverState;

    public Driver(Long id, String firstName, String lastName, String contactNumber, Location currentLocation, Vehicle vehicle, DriverState driverState) {
        super(id, firstName, lastName);
        this.contactNumber = contactNumber;
        this.currentLocation = currentLocation;
        this.vehicle = vehicle;
        this.driverState = driverState;
    }

    public Driver(String firstName, String lastName, String contactNumber, Location currentLocation, Vehicle vehicle, DriverState driverState) {
        super(firstName, lastName);
        this.contactNumber = contactNumber;
        this.currentLocation = currentLocation;
        this.vehicle = vehicle;
        this.driverState = driverState;
    }

    public String getContactNumber() { return contactNumber; }

    public Location getCurrentLocation() { return currentLocation; }

    public Vehicle getVehicle() { return vehicle; }

    @Override
    public DriverState getState() {
        return driverState;
    }

    @Override
    public void updateState(DriverState state) {
        driverState = state;
    }


}