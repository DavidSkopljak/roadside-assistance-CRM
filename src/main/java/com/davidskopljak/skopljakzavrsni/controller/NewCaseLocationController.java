package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseLocationException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.validation.Validators;
import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    private TextField postalCodeTextField;
    @FXML
    private TextField coordinatesXTextField;
    @FXML
    private TextField coordinatesYTextField;
    @FXML
    private AnchorPane mapContainer;

    private final MapView mapView = new MapView();

    String country;
    String city;
    String address;
    String postalCode;
    String coordinatesX;
    String coordinatesY;

    public void initialize(){
        Case caseInProgress = CRMApplication.getCaseInProgress();
        Location location = caseInProgress.getLocation();
        if (location != null) {
            setText(countryTextField, location.getCountry());
            setText(cityTextField, location.getCity());
            setText(addressTextField, location.getAddress());
            setText(postalCodeTextField, location.getPostalCode());
            setText(coordinatesXTextField, location.getCoordinatesX().toString());
            setText(coordinatesYTextField, location.getCoordinatesY().toString());
        }

        mapView.setMapType(MapType.OSM);
        mapView.initialize(Configuration.builder()
                .showZoomControls(true)
                .build());

        mapView.initializedProperty().addListener((obs, wasInitialized, isNowInitialized) -> {
            if (Boolean.TRUE.equals(isNowInitialized)) {
                mapView.setCenter(new Coordinate(45.8150, 15.9819));
                mapView.setZoom(13);

                mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
                    Coordinate coord = event.getCoordinate();

                    Marker marker = Marker.createProvided(Marker.Provided.RED)
                            .setPosition(coord)
                            .setVisible(true);
                    mapView.addMarker(marker);

                    setText(coordinatesXTextField, coord.getLongitude().toString());
                    setText(coordinatesYTextField, coord.getLatitude().toString());

                    Location clickedLocation = Location.getLocationFromCoordinates(BigDecimal.valueOf(coord.getLatitude()), BigDecimal.valueOf(coord.getLongitude()));
                    setText(addressTextField, clickedLocation.getAddress());
                    setText(cityTextField, clickedLocation.getCity());
                    setText(countryTextField, clickedLocation.getCountry());
                    setText(postalCodeTextField, clickedLocation.getPostalCode());
                });
            }
        });

        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);
        AnchorPane.setLeftAnchor(mapView, 0.0);

        mapContainer.getChildren().add(mapView);
    }

    private static void setText(TextField field, String value) {
        field.setText(value == null ? "" : value);
    }


    public void setLocationInfo() {
        country = countryTextField.getText();
        city = cityTextField.getText();
        address = addressTextField.getText();
        postalCode = postalCodeTextField.getText();
        coordinatesX = coordinatesXTextField.getText();
        coordinatesY = coordinatesYTextField.getText();

        try {
            validateCaseLocation();
            Location location = new Location(address, city, country, postalCode, new BigDecimal(coordinatesX), new BigDecimal(coordinatesY));
            Case newCase = CRMApplication.getCaseInProgress();
            newCase.setLocation(location);

        } catch (InvalidCaseLocationException e) {
            CRMApplication.log.error(e.getMessage());
            MiscHelpers.showAlert("Invalid location information. Please check the case details.", Alert.AlertType.WARNING);
        }
    }



    public void validateCaseLocation() throws InvalidCaseLocationException {
        if (Boolean.FALSE.equals(Validators.isValidString(List.of(country, city)))) {
            throw new InvalidCaseLocationException("Country, city or address contains invalid characters.");
        }

        if(Boolean.FALSE.equals(Validators.isValidStringWithNumbers(address))){
            throw new InvalidCaseLocationException("Address contains invalid characters.");
        }

        if (Boolean.FALSE.equals(Validators.isValidInt(postalCode))) {
            throw new InvalidCaseLocationException("Postal code cannot be empty.");
        }

        if (Boolean.FALSE.equals(Validators.isValidGeoCoords(coordinatesX + ", " + coordinatesY))) {
            throw new InvalidCaseLocationException("Coordinates must be valid decimal numbers.");
        }


    }
}
