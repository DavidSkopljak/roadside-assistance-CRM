package com.davidskopljak.skopljakzavrsni.entity;

// tow truck drivers
// needs to have lists of available equipment and quantity of available equipment
// e.g. crane, rollers, battery starter kit, ...
// also needs to have status field - available, en route to case(new case can be queued), unavailable
// needs current location - this can update every X minutes
// needs field for current vehicle
public class Driver extends Person {
    private String contactNumber;
    private Location currentLocation;
    private Vehicle vehicle;

    public Driver(Long id, String firstName, String lastName, String contactNumber, Location currentLocation, Vehicle vehicle) {
        super(id, firstName, lastName);
        this.contactNumber = contactNumber;
        this.currentLocation = currentLocation;
        this.vehicle = vehicle;
    }

    public Driver(String firstName, String lastName, String contactNumber, Location currentLocation, Vehicle vehicle) {
        super(firstName, lastName);
        this.contactNumber = contactNumber;
        this.currentLocation = currentLocation;
        this.vehicle = vehicle;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}