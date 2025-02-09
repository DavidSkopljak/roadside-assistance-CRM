package java.main.entity;

import java.main.enums.CauseOfVehicleDamage;
import java.main.enums.VehicleBrand;
import java.main.enums.VehicleDamageType;
import java.util.ArrayList;

// case, needs info about client, their vehicle, total number of people who need assistance,
// location of case, ability to assign driver, ability to add notes, cause of accident/vehicle damage, nature and description of
// vehicle damage, ability to assign state of case(no driver assigned, driver en route, cancelled, resolved)

//this class has required fields(Long id, Location location, Driver assignedDriver, Operator firstOperator, Vehicle clientVehicle, String damageDescription, CauseOfVehicleDamage causeOfDamage, VehicleDamageType damageType) and optional ones (caseNotes, lastEditedOperator)
public class Case extends Entity {
    private Location location;
    private Driver assignedDriver;
    private Operator firstOperator;
    private Operator lastEditedOperator;
    private Vehicle clientVehicle;
    private ArrayList<String> caseNotes;
    private String damageDescription;
    private CauseOfVehicleDamage causeOfDamage;
    private VehicleDamageType damageType;

    public Case(Long id, Location location, Driver assignedDriver, Operator firstOperator, Operator lastEditedOperator, Vehicle clientVehicle, ArrayList<String> caseNotes, String damageDescription, CauseOfVehicleDamage causeOfDamage, VehicleDamageType damageType) {
        super(id);
        this.location = location;
        this.assignedDriver = assignedDriver;
        this.firstOperator = firstOperator;
        this.lastEditedOperator = lastEditedOperator;
        this.clientVehicle = clientVehicle;
        this.caseNotes = caseNotes;
        this.damageDescription = damageDescription;
        this.causeOfDamage = causeOfDamage;
        this.damageType = damageType;
    }

    public Case(Long id, Location location, Driver assignedDriver, Operator firstOperator, Vehicle clientVehicle, String damageDescription, CauseOfVehicleDamage causeOfDamage, VehicleDamageType damageType) {
        super(id);
        this.location = location;
        this.assignedDriver = assignedDriver;
        this.firstOperator = firstOperator;
        this.clientVehicle = clientVehicle;
        this.damageDescription = damageDescription;
        this.causeOfDamage = causeOfDamage;
        this.damageType = damageType;
    }

    public static class Builder {
        private Location location;
        private Driver assignedDriver;
        private Operator firstOperator;
        private Operator lastEditedOperator;
        private Vehicle clientVehicle;
        private ArrayList<String> caseNotes = new ArrayList<>();
        private String damageDescription;
        private CauseOfVehicleDamage causeOfDamage;
        private VehicleDamageType damageType;
        public Builder() {}

        public Builder location(Location location) {}
    }

    public void setLastEditedOperator(Operator lastEditedOperator) {
        this.lastEditedOperator = lastEditedOperator;
    }

    public void setCaseNotes(ArrayList<String> caseNotes) {
        this.caseNotes = caseNotes;
    }

    public Location getLocation() {
        return location;
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
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

    public ArrayList<String> getCaseNotes() {
        return caseNotes;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public CauseOfVehicleDamage getCauseOfDamage() {
        return causeOfDamage;
    }

    public VehicleDamageType getDamageType() {
        return damageType;
    }
}
