package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageCause;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageType;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseInfoException;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseLocationException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.RepositoryHelper;
import com.davidskopljak.skopljakzavrsni.repository.*;
import com.davidskopljak.skopljakzavrsni.validation.Validators;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;

public class NewCaseController {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField contactNumberTextField;
    @FXML
    private TextField licensePlateTextField;
    @FXML
    private TextField vinTextField;
    @FXML
    private TextField damageDescriptionTextField;
    @FXML
    private DatePicker firstRegDateDatePicker;
    @FXML
    private ComboBox<VehicleModel> modelComboBox;
    @FXML
    private ComboBox<VehicleDamageType> damageTypeComboBox;
    @FXML
    private ComboBox<VehicleDamageCause> damageCauseComboBox;

    @FXML
    private TextField countryTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField houseNumberTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField coordinatesXTextField;
    @FXML
    private TextField coordinatesYTextField;

    String firstName;
    String lastName;
    String contactNumber;
    String licensePlate;
    String damageDescription;
    VehicleModel vehicleModel;
    String vinText;
    LocalDate firstRegLocalDate;
    VehicleDamageType damageType;
    VehicleDamageCause damageCause;

    String country;
    String city;
    String address;
    String houseNumber;
    String postalCode;
    String coordinatesX;
    String coordinatesY;

    public void initialize() throws SQLException {
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();){
                var models = RepositoryHelper.queryAllVehicleModels(conn);
                ObservableList<VehicleModel> modelList = FXCollections.observableArrayList(models);
                modelComboBox.setItems(modelList);

                var damageTypes = RepositoryHelper.queryAllVehicleDamageTypes(conn);
                ObservableList<VehicleDamageType> typeList = FXCollections.observableArrayList(damageTypes);
                damageTypeComboBox.setItems(typeList);

                var damageCauses = RepositoryHelper.queryAllVehicleDamageCauses(conn);
                ObservableList<VehicleDamageCause> causeList = FXCollections.observableArrayList(damageCauses);
                damageCauseComboBox.setItems(causeList);
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }
    }

    public void saveCase(ActionEvent event){
        try{
            setCaseInfo();
            //validateCaseLocation();
            Location location = new Location("address", "city", "country", "postalCode", new BigDecimal(45.8089772239981), new BigDecimal(15.716704654770382));

            ClientRepository clientRepository = new ClientRepository();
            Client client = new Client(firstName, lastName, contactNumber);

            VehicleRepository vehicleRepository = new VehicleRepository();
            Vehicle clientVehicle = new Vehicle(licensePlate, vehicleModel, vinText);


            OperatorRepository operatorRepository = new OperatorRepository();
            Operator firstOperator = operatorRepository.findById(140L);

            Operator lastEditedOperator = firstOperator;

            CaseState caseState = CaseState.ACTIVE;

            Case newCase = new Case();

            newCase.setClient(client)
                    .setClientVehicle(clientVehicle)
                    .setClientVehicleFirstRegistrationDate(LocalDate.now().minusDays(10))
                    .setDamageCause(damageCause)
                    .setDamageType(damageType)
                    .setFirstOperator(firstOperator)
                    .setLastEditedOperator(lastEditedOperator)
                    .setLocation(location)
                    .setDamageDescription(damageDescription)
                    .setCreatedDateTime(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));

            newCase.updateState(caseState);

            CaseRepository caseRepository = new CaseRepository();
            caseRepository.save(newCase);

        }catch (InvalidCaseInfoException e){
            CRMApplication.log.error(e.getMessage());
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("Invalid case information. Please check the case details.");
            a.show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*
        try{
            Client client = new Client(firstName, lastName, contactNumber);
            Vehicle clientVehicle = new Vehicle(licensePlate, vehicleModel, firstRegLocalDate, vinText);

            CaseRepository caseRepository = new CaseRepository();
            Case newCase;
            newCase.setClient(client)
                    .setClientVehicle(clientVehicle)
                    .setDamageCause(damageCause)
                    .setDamageType(damageType)
                    .setFirstOperator(CRMApplication.getCurrentOperator())
                    .setLastEditedOperator(CRMApplication.getCurrentOperator())
                    .setLocation(location)
                    .setDamageDescription(damageDescription)
                    .setCreatedDateTime(createdDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .setActiveService(activeService);

        }catch(SQLException e){
            CRMApplication.log.error(e.getMessage());
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setContentText("Something went wrong. Please try again.");
            a.show();
        }
        */

        //VehicleRepository.addToDatabase(new Vehicle(Validators.removeSpecialCharacters(licensePlate), vehicleModel, firstRegLocalDate, vinText));
        //LocationRepository.addToDatabase(/*new Location(location data)*/);
    }

    public void setCaseInfo() throws InvalidCaseInfoException{
        firstName = this.firstNameTextField.getText();
        lastName = this.lastNameTextField.getText();
        contactNumber = this.contactNumberTextField.getText();
        licensePlate = this.licensePlateTextField.getText();
        damageDescription = this.damageDescriptionTextField.getText();
        vehicleModel = this.modelComboBox.getValue();
        vinText = this.vinTextField.getText();
        firstRegLocalDate = this.firstRegDateDatePicker.getValue();
        damageType = this.damageTypeComboBox.getValue();
        damageCause = this.damageCauseComboBox.getValue();
        try{
            validateCaseInfo();
        }catch (InvalidCaseInfoException e){
            CRMApplication.log.error(e.getMessage());
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("Invalid case information. Please check the case details.");
            throw new InvalidCaseInfoException(e.getMessage());
        }
    }

    public Boolean validateCaseInfo() throws InvalidCaseInfoException {
        System.out.println("Validating not-null values...");
        if (Boolean.FALSE.equals(Validators.isNotNull(Arrays.asList(vehicleModel, firstRegLocalDate, damageType, damageCause)))) {
            System.out.println("❌ Not-null validation failed.");
            throw new InvalidCaseInfoException("Some required fields are null.");
        }

        /*System.out.println("Validating strings...");
        if (Boolean.FALSE.equals(Validators.isValidString(Arrays.asList(firstName, lastName, licensePlate, vinText, damageDescription)))) {
            System.out.println("❌ String validation failed.");
            throw new InvalidCaseInfoException("One or more string fields are invalid.");
        }*/


        if (!Validators.isValidString(firstName)) {
            System.out.println("❌ Invalid first name: " + firstName);
            throw new InvalidCaseInfoException("Invalid first name.");
        }

        if (!Validators.isValidString(lastName)) {
            System.out.println("❌ Invalid last name: " + lastName);
            throw new InvalidCaseInfoException("Invalid last name.");
        }

        if (!Validators.isValidString(damageDescription)) {
            System.out.println("❌ Invalid damage description: " + damageDescription);
            throw new InvalidCaseInfoException("Invalid damage description.");
        }

        System.out.println("Validating contact number...");
        if (!Validators.isValidHRPhoneNumber(contactNumber)) {
            System.out.println("❌ Phone number validation failed: " + contactNumber);
            throw new InvalidCaseInfoException("Invalid phone number.");
        }

        System.out.println("Validating VIN...");
        if (!Validators.isValidVIN(vinText)) {
            System.out.println("❌ VIN validation failed: " + vinText);
            throw new InvalidCaseInfoException("Invalid VIN.");
        }

        System.out.println("Validating license plate...");
        if (!Validators.isValidHRLicensePlateNumber(licensePlate)) {
            System.out.println("❌ License plate validation failed: " + licensePlate);
            throw new InvalidCaseInfoException("Invalid license plate number.");
        }

        return true;
    }

    public Boolean validateCaseLocation() throws InvalidCaseLocationException {
        //if(!(Validators.is))
        return true;
    }
}
