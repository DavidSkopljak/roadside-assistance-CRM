package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.DriverState;
import com.davidskopljak.skopljakzavrsni.interfaces.Trackable;

public class Driver extends Person implements Trackable<DriverState> {
    private String contactNumber;
    private DriverState driverState;

    public Driver(Long id, String firstName, String lastName, String contactNumber, DriverState driverState) {
        super(id, firstName, lastName);
        this.contactNumber = contactNumber;
        this.driverState = driverState;
    }

    public Driver(String firstName, String lastName, String contactNumber, DriverState driverState) {
        super(firstName, lastName);
        this.contactNumber = contactNumber;
        this.driverState = driverState;
    }

    public Driver(Long id, Driver driver) {
        super(id, driver.getFirstName(), driver.getLastName());
        this.contactNumber = driver.getContactNumber();
        this.driverState = driver.getState();
    }

    public String getContactNumber() { return contactNumber; }

    @Override
    public DriverState getState() {
        return driverState;
    }

    @Override
    public void updateState(DriverState state) {
        driverState = state;
    }


}