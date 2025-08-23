package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageCause;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageType;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseInfoException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.helpers.RepositoryHelper;
import com.davidskopljak.skopljakzavrsni.interfaces.CaseController;
import com.davidskopljak.skopljakzavrsni.repository.*;
import com.davidskopljak.skopljakzavrsni.validation.Validators;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CaseInfoController implements CaseController {
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
    private AnchorPane rootAnchorPane;

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

    private CaseWindowController caseWindowController;
    private CaseMenuController caseMenuController;

    public void setCaseWindowController(CaseWindowController caseWindowController){
        this.caseWindowController = caseWindowController;
        try {
            FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("case-menu.fxml"));
            Parent menuRoot = loader.load();
            caseMenuController = loader.getController();

            rootAnchorPane.getChildren().add(0, menuRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
        injectCaseMenuControllerWindowReference();

        if(caseWindowController.getActiveCase() != null){
            initializeActiveCase();
        } else{
            caseWindowController.setActiveCase(new Case());
        }
    }

    public void injectCaseMenuControllerWindowReference() {
        if (caseMenuController != null) {
            caseMenuController.setCaseWindowController(caseWindowController);
        }
    }

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
            throw new RepositoryAccessException("Failed to load vehicle models, damage types or damage causes from database: " + e.getMessage());
        }
    }

    private void initializeActiveCase() {
        Case caseInProgress = this.caseWindowController.getActiveCase();

        Client client = caseInProgress.getClient();
        if (client != null) {
            setText(firstNameTextField, client.getFirstName());
            setText(lastNameTextField, client.getLastName());
            setText(contactNumberTextField, client.getContactNumber());
        }

        Vehicle vehicle = caseInProgress.getClientVehicle();
        if (vehicle != null) {
            setText(licensePlateTextField, vehicle.getLicensePlate());
            setText(vinTextField, vehicle.getVin());
            modelComboBox.getSelectionModel().select(vehicle.getModel());
        }

        if (caseInProgress.getDamageDescription() != null)
            setText(damageDescriptionTextField, caseInProgress.getDamageDescription());

        if (caseInProgress.getClientVehicleFirstRegistrationDate() != null)
            firstRegDateDatePicker.setValue(caseInProgress.getClientVehicleFirstRegistrationDate());

        if (caseInProgress.getDamageType() != null)
            damageTypeComboBox.getSelectionModel().select(caseInProgress.getDamageType());

        if (caseInProgress.getDamageCause() != null)
            damageCauseComboBox.getSelectionModel().select(caseInProgress.getDamageCause());
    }


    public void setCase(Case existingCase){
        Client client = existingCase.getClient();
        if (client != null) {
            setText(firstNameTextField, client.getFirstName());
            setText(lastNameTextField, client.getLastName());
            setText(contactNumberTextField, client.getContactNumber());
        }

        Vehicle vehicle = existingCase.getClientVehicle();
        if (vehicle != null) {
            setText(licensePlateTextField, vehicle.getLicensePlate());
            setText(vinTextField, vehicle.getVin());
            modelComboBox.getSelectionModel().select(vehicle.getModel());
        }

        if (existingCase.getDamageDescription() != null)
            setText(damageDescriptionTextField, existingCase.getDamageDescription());

        if (existingCase.getClientVehicleFirstRegistrationDate() != null)
            firstRegDateDatePicker.setValue(existingCase.getClientVehicleFirstRegistrationDate());

        if (existingCase.getDamageType() != null)
            damageTypeComboBox.getSelectionModel().select(existingCase.getDamageType());

        if (existingCase.getDamageCause() != null)
            damageCauseComboBox.getSelectionModel().select(existingCase.getDamageCause());
    }

    private static void setText(TextField field, String value) {
        field.setText(value == null ? "" : value);
    }

    @FXML
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

        System.out.println("setting case info: " + firstName + lastName + contactNumber + licensePlate + damageDescription + vehicleModel + vinText + firstRegLocalDate + damageType + damageCause);

        try {
            validateCaseInfo();
        } catch (InvalidCaseInfoException e) {
            CRMApplication.log.error(e.getMessage());
            MiscHelpers.showAlert("Invalid case information. Please check the case details.", Alert.AlertType.WARNING);
            return;
        }

        Case newCase = caseWindowController.getActiveCase();
        if (newCase == null) {
            newCase = new Case();
        }

        Client client;
        if (newCase.getClient() != null) {
            client = new Client(
                    newCase.getClient().getId(),
                    firstName,
                    lastName,
                    contactNumber
            );
        } else {
            client = new Client(firstName, lastName, contactNumber);
        }

        Vehicle clientVehicle;
        if (newCase.getClientVehicle() != null) {
            clientVehicle = new Vehicle(
                    newCase.getClientVehicle().getId(),
                    licensePlate,
                    vehicleModel,
                    vinText
            );
        } else {
            clientVehicle = new Vehicle(licensePlate, vehicleModel, vinText);
        }
        Operator firstOperator = newCase.getFirstOperator() != null ?
                newCase.getFirstOperator() : CRMApplication.getActiveOperator();
        Operator lastOperator = CRMApplication.getActiveOperator();

        LocalDateTime createdTime = newCase.getCreatedDateTime() != null ?
                newCase.getCreatedDateTime() :
                LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);

        newCase.setClient(client)
                .setClientVehicle(clientVehicle)
                .setClientVehicleFirstRegistrationDate(firstRegLocalDate)
                .setDamageCause(damageCause)
                .setDamageType(damageType)
                .setFirstOperator(firstOperator)
                .setLastEditedOperator(lastOperator)
                .setDamageDescription(damageDescription)
                .setCreatedDateTime(createdTime);

        System.out.println("Setting newCase info with vehicle: " + newCase.getClientVehicle().getId() + " " + newCase.getClientVehicle().getModel());
        caseWindowController.setActiveCase(newCase);
    }


    public void validateCaseInfo() throws InvalidCaseInfoException {
        if (Boolean.FALSE.equals(Validators.isNotNull(Arrays.asList(vehicleModel, firstRegLocalDate, damageType, damageCause)))) {
            throw new InvalidCaseInfoException("Some required fields are null.");
        }

        if (Boolean.FALSE.equals(Validators.isValidString(List.of(firstName, lastName)))) {
            throw new InvalidCaseInfoException("Invalid client information.");
        }

        if (Boolean.FALSE.equals(Validators.isValidString(damageDescription))) {
            throw new InvalidCaseInfoException("Invalid vehicle damage description.");
        }

        if (Boolean.FALSE.equals(Validators.isValidHRPhoneNumber(contactNumber))) {
            throw new InvalidCaseInfoException("Invalid phone number.");
        }

        if (Boolean.FALSE.equals(Validators.isValidVIN(vinText))) {
            throw new InvalidCaseInfoException("Invalid VIN.");
        }

        if (Boolean.FALSE.equals(Validators.isValidHRLicensePlateNumber(licensePlate))) {
            throw new InvalidCaseInfoException("Invalid license plate number.");
        }
    }
}
