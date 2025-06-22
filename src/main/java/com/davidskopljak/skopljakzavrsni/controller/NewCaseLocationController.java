package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseLocationException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.validation.Validators;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.List;

public class NewCaseLocationController {

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

    String country;
    String city;
    String address;
    String houseNumber;
    String postalCode;
    String coordinatesX;
    String coordinatesY;

    public void setLocationInfo() {
        country = countryTextField.getText();
        city = cityTextField.getText();
        address = addressTextField.getText();
        houseNumber = houseNumberTextField.getText();
        postalCode = postalCodeTextField.getText();
        coordinatesX = coordinatesXTextField.getText();
        coordinatesY = coordinatesYTextField.getText();

        try {
            validateCaseLocation();
            Location location = new Location(address, city, country, postalCode, new BigDecimal(coordinatesX), new BigDecimal(coordinatesY));
            Case newCase = CRMApplication.getUnfinishedNewCase();
            newCase.setLocation(location);

        } catch (InvalidCaseLocationException e) {
            CRMApplication.log.error(e.getMessage());
            MiscHelpers.showAlert("Invalid location information. Please check the case details.", Alert.AlertType.WARNING);
        }
    }



    public void validateCaseLocation() throws InvalidCaseLocationException {
        if (!Validators.isValidString(List.of(country, city, address, houseNumber))) {
            throw new InvalidCaseLocationException("Country, city, address or house number contains invalid characters.");
        }

        if (!Validators.isValidInt(postalCode)) {
            throw new InvalidCaseLocationException("Postal code cannot be empty.");
        }

        if (!Validators.isValidGeoCoords(coordinatesX + ", " + coordinatesY)) {
            throw new InvalidCaseLocationException("Coordinates must be valid decimal numbers.");
        }


    }
}
