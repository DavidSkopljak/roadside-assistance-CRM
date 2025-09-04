package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Location;
import com.davidskopljak.skopljakzavrsni.entity.Workshop;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import com.davidskopljak.skopljakzavrsni.exceptions.InvalidWorkshopInfoException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.helpers.RepositoryHelper;
import com.davidskopljak.skopljakzavrsni.repository.DatabaseConnectionManager;
import com.davidskopljak.skopljakzavrsni.repository.WorkshopRepository;
import com.davidskopljak.skopljakzavrsni.validation.Validators;
import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import com.sothawo.mapjfx.Configuration;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ManageWorkshopsController {
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField nameTextField;

    @FXML
    private ComboBox<VehicleModel> permittedVehicleModelComboBox;

    @FXML
    private AnchorPane locationAnchorPane;

    @FXML
    private Button confirmButton;

    @FXML
    private TableView<Workshop> workshopsTable;

    @FXML
    private TableColumn<Workshop, String> idColumn;

    @FXML
    private TableColumn<Workshop, String> nameColumn;

    @FXML
    private TableColumn<Workshop, String> vehicleModelColumn;

    @FXML
    private TableColumn<Workshop, String> locationColumn;

    @FXML
    private TableColumn<Workshop, String> deleteWorkshopColumn;

    @FXML
    private TextArea filterTextArea;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private AnchorPane mapContainer;

    @FXML
    private TextField longitudeTextField;

    @FXML
    private TextField latitudeTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField postalCodeTextField;



    private final Image deleteImage = new Image(getClass().getResourceAsStream("/images/x-transparent.png"));
    private static final Coordinate DEFAULT_MAPVIEW_LOCATION = new Coordinate(45.8150, 15.9819);
    private Marker currentMarker;

    private final MapView mapView = new MapView();

    public void handleAddNewWorkshop(){
        try{
            validateWorkshop();
            saveNewWorkshop();
        } catch(InvalidWorkshopInfoException e){
            CRMApplication.log.error("Failed to add new workshop: ", e);
        }
    }

    public void initialize(){
        filterTextArea.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode() == KeyCode.ENTER) {
                ke.consume();
                MiscHelpers.runThread(() -> {
                    try {
                        WorkshopRepository workshopRepository = new WorkshopRepository();
                        List<Workshop> allWorkshops = workshopRepository.findAll();
                        final List<Workshop> results;

                        String selectedColumn = filterComboBox.getSelectionModel().getSelectedItem();
                        String filter = filterTextArea.getText().trim().toLowerCase();

                        if (selectedColumn == null) {
                            results = matchAnyColumn(allWorkshops, filter);
                        } else {
                            results = matchColumn(allWorkshops, filter, selectedColumn);
                        }

                        javafx.application.Platform.runLater(() -> workshopsTable.getItems().setAll(results));
                    } catch (Exception e) {
                        CRMApplication.log.error("Search failed", e);
                    }
                });
            }
        });

        filterComboBox.getItems().addAll(
                "ID",
                "Name",
                "Vehicle Model",
                "Location"
        );

        initializeMapView();

        setupWorkshopTable();
        loadVehicleModels();
    }

    private List<Workshop> matchAnyColumn(List<Workshop> allWorkshops, String filter){
        List<Workshop> results = new ArrayList<>();
        for(Workshop w : allWorkshops){
            if (String.valueOf(w.getId()).contains(filter)
                    || w.getName().toLowerCase().contains(filter)
                    || (w.getPermittedVehicleModel() != null && w.getPermittedVehicleModel().toString().toLowerCase().contains(filter))
                    || (w.getLocation() != null && w.getLocation().toString().toLowerCase().contains(filter))){
                results.add(w);
            }
        }
        return results;
    }

    private List<Workshop> matchColumn(List<Workshop> workshops, String filter, String column) {
        List<Workshop> results = new ArrayList<>();
        java.util.function.Function<Workshop, String> columnExtractor;

        switch (column) {
            case "ID" -> columnExtractor = w -> String.valueOf(w.getId());
            case "Name" -> columnExtractor = Workshop::getName;
            case "Vehicle Model" -> columnExtractor = w -> w.getPermittedVehicleModel() != null ? w.getPermittedVehicleModel().toString() : "";
            case "Location" -> columnExtractor = w -> w.getLocation() != null ? w.getLocation().toString() : "";
            default -> columnExtractor = w -> "";
        }

        for (Workshop w : workshops) {
            String value = columnExtractor.apply(w);
            if (value != null && value.toLowerCase().contains(filter)) {
                results.add(w);
            }
        }

        return results;
    }

    private void setupWorkshopTable() {
        idColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId().toString()));
        nameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        vehicleModelColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getPermittedVehicleModel() != null ?
                                cellData.getValue().getPermittedVehicleModel().toString() : ""));
        locationColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getLocation() != null ?
                                cellData.getValue().getLocation().toString() : ""));

        deleteWorkshopColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setOnMouseClicked(null);
                } else {
                    ImageView deleteIcon = new ImageView(deleteImage);
                    deleteIcon.setFitWidth(16);
                    deleteIcon.setFitHeight(16);
                    setGraphic(deleteIcon);

                    setOnMouseClicked(event ->
                            handleDeleteWorkshop(getTableView().getItems().get(getIndex()))
                    );
                }
            }
        });

        refreshWorkshopTable();
    }

    private void loadVehicleModels() {
        MiscHelpers.runThread(() -> {
            try (Connection conn = DatabaseConnectionManager.getInstance().getConnection()){
                List<VehicleModel> vehicleModels = RepositoryHelper.queryAllVehicleModels(conn);
                javafx.application.Platform.runLater(() ->
                        permittedVehicleModelComboBox.getItems().setAll(vehicleModels));
            } catch (Exception e) {
                CRMApplication.log.error("Failed to load vehicle models from database: ", e);
            }

        });
    }

    private void refreshWorkshopTable() {
        MiscHelpers.runThread(() -> {
            WorkshopRepository workshopRepository = new WorkshopRepository();
            try {
                List<Workshop> workshops = workshopRepository.findAll();
                javafx.application.Platform.runLater(() -> workshopsTable.getItems().setAll(workshops));
            } catch (RepositoryAccessException e) {
                CRMApplication.log.error("Failed to load workshops from database: ", e);
            }
        });
    }

    private void validateWorkshop() throws InvalidWorkshopInfoException {
        String name = nameTextField.getText().trim();
        VehicleModel selectedVehicleModel = permittedVehicleModelComboBox.getSelectionModel().getSelectedItem();
        Location location = (Location)  locationAnchorPane.getUserData();

        if(Boolean.FALSE.equals(Validators.isValidString(name))
                || selectedVehicleModel == null){
            throw new InvalidWorkshopInfoException("Invalid workshop info.");
        }
    }

    private void saveNewWorkshop(){
        MiscHelpers.runThread(() -> {
            WorkshopRepository workshopRepository = new WorkshopRepository();
            try{
                Workshop newWorkshop = new Workshop(
                        nameTextField.getText().trim(),
                        null, // Location will be set separately via your custom component
                        permittedVehicleModelComboBox.getSelectionModel().getSelectedItem()
                );
                workshopRepository.save(newWorkshop);
                refreshWorkshopTable();

                nameTextField.clear();
                permittedVehicleModelComboBox.getSelectionModel().clearSelection();

            } catch (RepositoryAccessException e){
                CRMApplication.log.error("Failed to save new workshop: ", e);
            }
        });
    }

    private void handleDeleteWorkshop(Workshop workshop){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this workshop?");
        alert.setContentText(workshop.getName());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                MiscHelpers.runThread(() -> {
                    WorkshopRepository workshopRepository = new WorkshopRepository();
                    try{
                        workshopRepository.deleteById(workshop.getId());
                        refreshWorkshopTable();
                    } catch(RepositoryAccessException e){
                        CRMApplication.log.error("Failed to delete workshop", e);
                        MiscHelpers.showAlert("Failed to delete workshop: " + e.getMessage(), Alert.AlertType.ERROR);
                    }
                });
            }
        });
    }

    private void initializeMapView(){
        mapView.setMapType(MapType.OSM);
        mapView.initialize(Configuration.builder()
                .showZoomControls(true)
                .build());

        mapView.initializedProperty().addListener((_, _, isNowInitialized) -> {
            if (Boolean.TRUE.equals(isNowInitialized)) {
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

}