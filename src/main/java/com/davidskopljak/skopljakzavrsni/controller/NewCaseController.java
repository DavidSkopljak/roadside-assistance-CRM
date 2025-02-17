package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Client;
import com.davidskopljak.skopljakzavrsni.entity.Vehicle;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageCause;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageType;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseInfoException;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseLocationException;
import com.davidskopljak.skopljakzavrsni.validation.Validators;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class NewCaseController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField contactNumberField;
    @FXML
    private TextField licensePlateField;
    @FXML
    private TextField vinTextField;
    @FXML
    private TextField damageDescriptionField;
    @FXML
    private DatePicker firstRegDateDatePicker;
    @FXML
    private ComboBox<VehicleModel> modelComboBox;
    @FXML
    private ComboBox<VehicleDamageType> damageTypeComboBox;
    @FXML
    private ComboBox<VehicleDamageCause> damageCauseComboBox;

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


    public void saveCase(){
        try{
            validateCaseInfo();
            validateCaseLocation();
        }catch (InvalidCaseInfoException e){
            CRMApplication.log.error(e.getMessage());
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("Invalid case information. Please check the case details.");
            return;
        }catch (InvalidCaseLocationException e){
            CRMApplication.log.error(e.getMessage());
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("Invalid case location. Please check the location details.");
        }
        //ClientRepository.addToDatabase(new Client(firstName, lastName, contactNumber));
        //VehicleRepository.addToDatabase(new Vehicle(Validators.removeSpecialCharacters(licensePlate), vehicleModel, firstRegLocalDate, vinText));
        //LocationRepository.addToDatabase(/*new Location(location data)*/);
    }

    public void setCaseInfo(){
        firstName = this.firstNameField.getText();
        lastName = this.lastNameField.getText();
        contactNumber = this.contactNumberField.getText();
        licensePlate = this.licensePlateField.getText();
        damageDescription = this.damageDescriptionField.getText();
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
        }
    }
    public Boolean validateCaseInfo() throws InvalidCaseInfoException {
        if(Boolean.FALSE.equals(Validators.isNotNull(Arrays.asList(vehicleModel, firstRegLocalDate, damageType, damageCause)))){
            throw new InvalidCaseInfoException();
        }

        //check if all strings have allowed characters and arent empty
        if (Boolean.FALSE.equals(Validators.isValidString(Arrays.asList(firstName, lastName, licensePlate, vinText, damageDescription)))){
            throw new InvalidCaseInfoException();
        }

        //check if contact number is formatted correctly
        if (!Validators.isValidHRPhoneNumber(contactNumber)){
            throw new InvalidCaseInfoException();
        }

        //check if vin is formatted correctly
        if(!Validators.isValidVIN(vinText)){
            throw new InvalidCaseInfoException();
        }

        //check if license plate is formatted correctly
        if(!Validators.isValidHRLicensePlateNumber(licensePlate)){
            throw new InvalidCaseInfoException();
        }
        return true;
    }

    public Boolean validateCaseLocation() throws InvalidCaseLocationException {
        //if(!(Validators.is))
        return true;
    }
}
