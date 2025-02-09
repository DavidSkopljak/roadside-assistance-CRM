package java.main.entity;

// tow truck drivers
// needs to have lists of available equipment and quantity of available equipment
// e.g. crane, rollers, battery starter kit, ...
// also needs to have status field - available, en route to case(new case can be queued), unavailable
// needs current location - this can update every X minutes
// needs field for current vehicle
public class Driver {
    private String contactNumber;
    private Location currentLocation;
    private Vehicle vehicle;

    public Driver(String contactNumber, Location currentLocation, Vehicle vehicle) {
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
