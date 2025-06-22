package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.enums.CaseState;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageCause;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageType;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseInfoException;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseLocationException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
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

    public void setCaseInfo() {

        firstName = firstNameTextField.getText();
        lastName = lastNameTextField.getText();
        contactNumber = contactNumberTextField.getText();
        licensePlate = licensePlateTextField.getText();
        damageDescription = damageDescriptionTextField.getText();
        vehicleModel = modelComboBox.getValue();
        vinText = vinTextField.getText();
        firstRegLocalDate = firstRegDateDatePicker.getValue();
        damageType = damageTypeComboBox.getValue();
        damageCause = damageCauseComboBox.getValue();

        try {
            validateCaseInfo();
        } catch (InvalidCaseInfoException e) {
            CRMApplication.log.error(e.getMessage());
            MiscHelpers.showAlert("Invalid case information. Please check the case details.", Alert.AlertType.WARNING);
            return;
        }

        Case newCase = CRMApplication.getUnfinishedNewCase();

        Client client = new Client(firstName, lastName, contactNumber);
        Vehicle clientVehicle = new Vehicle(licensePlate, vehicleModel, vinText);
        Operator firstOperator = CRMApplication.getActiveOperator();

        newCase.setClient(client)
                .setClientVehicle(clientVehicle)
                .setClientVehicleFirstRegistrationDate(firstRegLocalDate)
                .setDamageCause(damageCause)
                .setDamageType(damageType)
                .setFirstOperator(firstOperator)
                .setLastEditedOperator(firstOperator)
                .setDamageDescription(damageDescription)
                .setCreatedDateTime(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
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
}
