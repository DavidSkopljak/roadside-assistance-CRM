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
import java.util.List;
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

    public void saveCase(){
        try{
            validateCaseInfo();
            //validateLocationInfo();
            Location location = new Location("address", "city", "country", "postalCode", new BigDecimal(45.8089772239981), new BigDecimal(15.716704654770382));
            Client client = new Client(firstName, lastName, contactNumber);
            Vehicle clientVehicle = new Vehicle(licensePlate, vehicleModel, vinText);
            Operator firstOperator = CRMApplication.getActiveOperator();
            CaseState caseState = CaseState.ACTIVE;

            Case newCase = new Case();

            newCase.setClient(client)
                    .setClientVehicle(clientVehicle)
                    .setClientVehicleFirstRegistrationDate(LocalDate.now().minusDays(10))
                    .setDamageCause(damageCause)
                    .setDamageType(damageType)
                    .setFirstOperator(firstOperator)
                    .setLastEditedOperator(firstOperator)
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
            throw new RepositoryAccessException("Failed to save new case: " + e);
        }
    }

    public void setCaseInfo(){
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
    }

    public void setLocationInfo() {
        country = countryTextField.getText();
        city = cityTextField.getText();
        address = addressTextField.getText();
        houseNumber = houseNumberTextField.getText();
        postalCode = postalCodeTextField.getText();
        coordinatesX = coordinatesXTextField.getText();
        coordinatesY = coordinatesYTextField.getText();
    }


    public void validateCaseInfo() throws InvalidCaseInfoException {
        if (Boolean.FALSE.equals(Validators.isNotNull(Arrays.asList(vehicleModel, firstRegLocalDate, damageType, damageCause)))) {
            throw new InvalidCaseInfoException("Some required fields are null.");
        }

        List<String> fields = List.of(firstName, lastName, damageDescription);
        if (!Validators.isValidString(fields)) {
            throw new InvalidCaseInfoException("Invalid first name.");
        }

        if (!Validators.isValidHRPhoneNumber(contactNumber)) {
            throw new InvalidCaseInfoException("Invalid phone number.");
        }

        if (!Validators.isValidVIN(vinText)) {
            throw new InvalidCaseInfoException("Invalid VIN.");
        }

        if (!Validators.isValidHRLicensePlateNumber(licensePlate)) {
            throw new InvalidCaseInfoException("Invalid license plate number.");
        }
    }

    public Boolean validateCaseLocation() throws InvalidCaseLocationException {
        //if(!(Validators.is))
        return true;
    }

    public void resetForm() {
        firstNameTextField.clear();
        lastNameTextField.clear();
        contactNumberTextField.clear();
        licensePlateTextField.clear();
        vinTextField.clear();
        damageDescriptionTextField.clear();
        firstRegDateDatePicker.setValue(null);
        modelComboBox.getSelectionModel().clearSelection();
        damageTypeComboBox.getSelectionModel().clearSelection();
        damageCauseComboBox.getSelectionModel().clearSelection();
        countryTextField.clear();
        cityTextField.clear();
        addressTextField.clear();
        houseNumberTextField.clear();
        postalCodeTextField.clear();
        coordinatesXTextField.clear();
        coordinatesYTextField.clear();
    }

    public void outputForm() {
        System.out.println("First name: " + firstName);
        System.out.println("Last name: " + lastName);
        System.out.println("Contact number: " + contactNumber);
        System.out.println("License plate: " + licensePlate);
        System.out.println("VIN: " + vinText);
        System.out.println("Damage description: " + damageDescription);
        System.out.println("First registration date: " + firstRegLocalDate);
        System.out.println("Vehicle model: " + vehicleModel);
        System.out.println("Damage type: " + damageType);
        System.out.println("Damage cause: " + damageCause);
        System.out.println("Country: " + country);
        System.out.println("City: " + city);
        System.out.println("Address: " + address);
        System.out.println("House number: " + houseNumber);
        System.out.println("Postal code: " + postalCode);
        System.out.println("Coordinates X: " + coordinatesX);
        System.out.println("Coordinates Y: " + coordinatesY);
        System.out.println();
    }

}
