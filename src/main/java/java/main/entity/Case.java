package java.main.entity;

import java.main.enums.CaseState;
import java.util.ArrayList;

// case, needs info about client, their vehicle, total number of people who need assistance,
// location of case, ability to assign driver, ability to add notes, cause of accident/vehicle damage, nature and description of
// vehicle damage, ability to assign state of case(no driver assigned, driver en route, cancelled, resolved)

//this class has required fields(Long id, Location location, Driver assignedDriver, Operator firstOperator, Vehicle clientVehicle, String damageDescription, CauseOfVehicleDamage causeOfDamage, VehicleDamageType damageType) and optional ones (caseNotes, lastEditedOperator)
public class Case extends Entity {
    private Location location;
    private Operator firstOperator;
    private Operator lastEditedOperator;
    private Vehicle clientVehicle;
    private ArrayList<String> caseNotes = new ArrayList<>();
    private String damageDescription;
    private ArrayList<Service> services = new ArrayList<>();
    private CaseState caseState;

    public Case(Long id, Location location, Operator firstOperator, Vehicle clientVehicle, String damageDescription, CaseState caseState) {
        super(id);
        this.location = location;
        this.firstOperator = firstOperator;
        this.clientVehicle = clientVehicle;
        this.damageDescription = damageDescription;
        this.caseState = caseState;
    }

    public void addCaseNote(String note) {
        caseNotes.add(note);
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public CaseState getCaseState() {
        return caseState;
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

    public ArrayList<String> getCaseNotes() {
        return caseNotes;
    }

    public String getDamageDescription() {
        return damageDescription;
    }
}
