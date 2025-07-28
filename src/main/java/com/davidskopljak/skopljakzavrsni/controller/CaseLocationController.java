    package com.davidskopljak.skopljakzavrsni.controller;

    import com.davidskopljak.skopljakzavrsni.entity.*;
    import com.davidskopljak.skopljakzavrsni.exceptions.InvalidCaseLocationException;
    import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
    import com.davidskopljak.skopljakzavrsni.interfaces.CaseController;
    import com.davidskopljak.skopljakzavrsni.validation.Validators;
    import com.sothawo.mapjfx.*;
    import com.sothawo.mapjfx.event.MapViewEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.control.Alert;
    import javafx.scene.control.TextField;
    import javafx.scene.layout.AnchorPane;

    import java.io.IOException;
    import java.math.BigDecimal;
    import java.util.List;

    public class CaseLocationController implements CaseController {
        @FXML
        private TextField countryTextField;
        @FXML
        private TextField cityTextField;
        @FXML
        private TextField addressTextField;
        @FXML
        private TextField postalCodeTextField;
        @FXML
        private TextField latitudeTextField;
        @FXML
        private TextField longitudeTextField;
        @FXML
        private AnchorPane mapContainer;
        @FXML
        private AnchorPane rootAnchorPane;

        private static final Coordinate DEFAULT_MAPVIEW_LOCATION = new Coordinate(45.8150, 15.9819);
        private final MapView mapView = new MapView();
        private CaseWindowController caseWindowController;
        private CaseMenuController caseMenuController;
        private Marker currentMarker;

        String country;
        String city;
        String address;
        String postalCode;
        String latitude;
        String longitude;

        public void setCaseWindowController(CaseWindowController caseWindowController){
            this.caseWindowController = caseWindowController;
            try {
                FXMLLoader loader = new FXMLLoader(CRMApplication.class.getResource("case-menu.fxml"));
                Parent menuRoot = loader.load();
                caseMenuController = loader.getController();
                rootAnchorPane.getChildren().add(0, menuRoot); // Add at index 0 to put it at the top
            } catch (IOException e) {
                e.printStackTrace();
            }
            injectCaseMenuControllerWindowReference();
        }

        public void injectCaseMenuControllerWindowReference() {
            if (caseMenuController != null && caseWindowController != null) {
                caseMenuController.setCaseWindowController(caseWindowController);
            }
        }

        public void initialize(){
            mapView.setMapType(MapType.OSM);
            mapView.initialize(Configuration.builder()
                    .showZoomControls(true)
                    .build());

            mapView.initializedProperty().addListener((_, _, isNowInitialized) -> {
                if (Boolean.TRUE.equals(isNowInitialized)) {
                    Location location = caseWindowController.getActiveCase().getLocation();
                    if(location != null){
                        updateFieldsAndMap(location);
                    }else{
                        mapView.setCenter(DEFAULT_MAPVIEW_LOCATION);
                        mapView.setZoom(13);
                    }

                    mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
                        Coordinate coord = event.getCoordinate();

                        if (currentMarker != null) {
                            mapView.removeMarker(currentMarker);
                        }

                        currentMarker = Marker.createProvided(Marker.Provided.RED)
                                .setPosition(coord)
                                .setVisible(true);

                        mapView.addMarker(currentMarker);

                        setText(latitudeTextField, coord.getLatitude().toString());
                        setText(longitudeTextField, coord.getLongitude().toString());

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

        public void onUseAddressClicked() {
            System.out.println("inside onUseAddressCliecked");

            String address = addressTextField.getText();
            String city = cityTextField.getText();
            String country = countryTextField.getText();
            String postalCode = postalCodeTextField.getText();

            try {
                Location location = Location.getLocationFromAddress(country, city, postalCode, address);
                updateFieldsAndMap(location);
            } catch (Exception e) {
                MiscHelpers.showAlert("Could not resolve location from address. " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }

        public void onUseCoordinatesClicked() {
            System.out.println("inside onUseCoordinatesClicked");
            try {
                BigDecimal latitude = new BigDecimal(latitudeTextField.getText());
                BigDecimal longitude = new BigDecimal(longitudeTextField.getText());

                Location location = Location.getLocationFromCoordinates(latitude, longitude);
                updateFieldsAndMap(location);
            } catch (Exception e) {
                MiscHelpers.showAlert("Could not resolve location from longitude. " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }


        private void updateFieldsAndMap(Location location) {
            System.out.println("inside updateFieldsAndMap");
            if (location == null) return;
            System.out.println("Location is not null");
            setText(countryTextField, location.getCountry());
            setText(cityTextField, location.getCity());
            setText(addressTextField, location.getAddress());
            setText(postalCodeTextField, location.getPostalCode());
            setText(latitudeTextField, location.getLatitude().toString());
            setText(longitudeTextField, location.getLongitude().toString());

            Coordinate coord = new Coordinate(location.getLatitude().doubleValue(), location.getLongitude().doubleValue());

            if (currentMarker != null) {
                mapView.removeMarker(currentMarker);
            }

            currentMarker = Marker.createProvided(Marker.Provided.RED)
                    .setPosition(coord)
                    .setVisible(true);

            mapView.addMarker(currentMarker);

            mapView.setCenter(coord);
            mapView.setZoom(17);
        }

        private static void setText(TextField field, String value) {
            field.setText(value == null ? "" : value);
        }


        public void setLocationInfo() {
            country = countryTextField.getText();
            city = cityTextField.getText();
            address = addressTextField.getText();
            postalCode = postalCodeTextField.getText();
            latitude = latitudeTextField.getText();
            longitude = longitudeTextField.getText();

            try {
                validateCaseLocation();

                Case activeCase = caseWindowController.getActiveCase();
                if (activeCase == null) {
                    activeCase = new Case();
                }

                Location existingLocation = activeCase.getLocation();
                Location location;

                if (existingLocation != null) {
                    location = new Location(
                            existingLocation.getId(),
                            address,
                            city,
                            country,
                            postalCode,
                            new BigDecimal(latitude),
                            new BigDecimal(longitude)
                    );
                } else {
                    location = new Location(
                            address,
                            city,
                            country,
                            postalCode,
                            new BigDecimal(latitude),
                            new BigDecimal(longitude)
                    );
                }

                activeCase.setLocation(location);
                caseWindowController.setActiveCase(activeCase);

            } catch(InvalidCaseLocationException e) {
                CRMApplication.log.error(e.getMessage());
                MiscHelpers.showAlert("Invalid location information. Please check the case details - " + e.getMessage(), Alert.AlertType.WARNING);
            }
        }




        public void validateCaseLocation() throws InvalidCaseLocationException {
            if (Boolean.FALSE.equals(Validators.isValidString(List.of(country, city)))) {
                throw new InvalidCaseLocationException("Country, city or address contains invalid characters.");
            }

            if(Boolean.FALSE.equals(Validators.isValidAddress(address))){
                throw new InvalidCaseLocationException("Address contains invalid characters.");
            }

            if (Boolean.FALSE.equals(Validators.isValidInt(postalCode))) {
                throw new InvalidCaseLocationException("Postal code cannot be empty.");
            }

            if (Boolean.FALSE.equals(Validators.isValidGeoCoords(latitude + ", " + longitude))) {
                throw new InvalidCaseLocationException("Coordinates must be valid decimal numbers.");
            }


        }
    }
