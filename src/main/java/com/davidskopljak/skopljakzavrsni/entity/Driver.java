package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.DriverState;
import com.davidskopljak.skopljakzavrsni.interfaces.Trackable;

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

    public Driver(Long id, Driver driver) {
        super(id, driver.getFirstName(), driver.getLastName());
        this.contactNumber = driver.getContactNumber();
        this.currentLocation = driver.getCurrentLocation();
        this.vehicle = driver.getVehicle();
        this.driverState = driver.getState();
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