package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.enums.BufferedChangeType;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.Method;
import java.sql.Ref;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CaseDiffController {
    @FXML
    public TableView<FieldDiff> originalTable;
    @FXML
    public TableView<FieldDiff> modifiedTable;
    @FXML
    public TableColumn<FieldDiff, String> originalFieldColumn;
    @FXML
    public TableColumn<FieldDiff, String> modifiedFieldColumn;
    @FXML
    public TableColumn<FieldDiff, String> originalValueColumn;
    @FXML
    public TableColumn<FieldDiff, String> modifiedValueColumn;
    @FXML
    public ComboBox<String> caseComboBox;

    private ObservableList<FieldDiff> originalFieldDiffs = FXCollections.observableArrayList();
    private ObservableList<FieldDiff> modifiedFieldDiffs = FXCollections.observableArrayList();
    private ObservableList<String> caseItems = FXCollections.observableArrayList();

    private List<BufferableEntity> bufferableEntityList = new ArrayList<>();

    public void initialize() {
        setupTableColumns();
        loadBufferedCases();
        setupComboBox();
    }

    private void setupTableColumns() {
        originalFieldColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFieldName()));
        originalValueColumn.setCellValueFactory(data ->
                new SimpleStringProperty(formatValue(data.getValue().getOriginalValue())));

        modifiedFieldColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFieldName()));
        modifiedValueColumn.setCellValueFactory(data ->
                new SimpleStringProperty(formatValue(data.getValue().getModifiedValue())));

        originalTable.setItems(originalFieldDiffs);
        modifiedTable.setItems(modifiedFieldDiffs);
    }

    private void setupComboBox() {
        caseComboBox.setItems(caseItems);
        caseComboBox.setOnAction(e -> {
            int selectedIndex = caseComboBox.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < bufferableEntityList.size()) {
                displayCaseDiff(bufferableEntityList.get(selectedIndex));
            }
        });

        if (!caseItems.isEmpty()) {
            caseComboBox.getSelectionModel().selectFirst();
            displayCaseDiff(bufferableEntityList.get(0));
        }
    }

    private void loadBufferedCases() {
        try {
            EntityBuffer<Long, Case> caseBuffer = CRMApplication.getCaseBuffer();
            Map<BufferedChangeType, Map<Long, BufferableEntity<Long, Case>>> caseDiffs = caseBuffer.getBufferedEntities();


            for (BufferedChangeType changeType : caseDiffs.keySet()) {
                Map<Long, BufferableEntity<Long, Case>> entitiesMap = caseDiffs.get(changeType);

                for(Long bufferKey: entitiesMap.keySet()){
                    BufferableEntity<Long, Case> bufferableEntity = entitiesMap.get(bufferKey);

                    if (bufferableEntity != null && bufferableEntity.getEntity() != null) {
                        Case bufferedCase = bufferableEntity.getEntity();
                        String bufferKeyString = bufferedCase.getId() != null ? bufferKey.toString() : "";

                        String displayText = "Case " + bufferKeyString + " (" + changeType + ") - " +
                                (bufferedCase.getDamageDescription() != null ? bufferedCase.getDamageDescription().substring(0, Math.min(30, bufferedCase.getDamageDescription().length())) + "..." : "No description");

                        caseItems.add(displayText);
                        bufferableEntityList.add(bufferableEntity);

                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void displayCaseDiff(BufferableEntity<Long, Case> bufferableEntity) {
        originalFieldDiffs.clear();
        modifiedFieldDiffs.clear();

        try {
            List<FieldDiff> diffs = generateAllFieldDiffs(bufferableEntity);

            for (FieldDiff diff : diffs) {
                originalFieldDiffs.add(diff);
                modifiedFieldDiffs.add(diff);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<FieldDiff> generateAllFieldDiffs(BufferableEntity<Long, Case> bufferableEntity) {
        List<FieldDiff> diffs = new ArrayList<>();

        Case bufferedCase = bufferableEntity.getEntity();
        Case originalCase = null;

        if (bufferableEntity.getType() != BufferedChangeType.NEW && bufferedCase.getId() != null) {
            try {
                CaseRepository caseRepository = new CaseRepository();
                originalCase = caseRepository.findById(bufferedCase.getId());
            } catch (SQLException e) {
                CRMApplication.log.error("error reading case " + bufferedCase.getId(), e);
            }
        }

        diffs.add(new FieldDiff("ID",
                originalCase != null ? originalCase.getId() : null,
                bufferedCase.getId()));
        diffs.add(new FieldDiff("Damage Description",
                originalCase != null ? originalCase.getDamageDescription() : null,
                bufferedCase.getDamageDescription()));
        diffs.add(new FieldDiff("Case State",
                originalCase != null ? originalCase.getState() : null,
                bufferedCase.getState()));
        diffs.add(new FieldDiff("Damage Type",
                originalCase != null ? originalCase.getDamageType() : null,
                bufferedCase.getDamageType()));
        diffs.add(new FieldDiff("Damage Cause",
                originalCase != null ? originalCase.getDamageCause() : null,
                bufferedCase.getDamageCause()));
        diffs.add(new FieldDiff("Created Date",
                originalCase != null ? originalCase.getCreatedDateTime() : null,
                bufferedCase.getCreatedDateTime()));
        diffs.add(new FieldDiff("Vehicle Registration Date",
                originalCase != null ? originalCase.getClientVehicleFirstRegistrationDate() : null,
                bufferedCase.getClientVehicleFirstRegistrationDate()));

        Location origLoc = originalCase != null ? originalCase.getLocation() : null;
        Location bufLoc = bufferedCase.getLocation();
        diffs.add(new FieldDiff("Location ID", origLoc != null ? origLoc.getId() : null, bufLoc != null ? bufLoc.getId() : null));
        diffs.add(new FieldDiff("Location Address", origLoc != null ? origLoc.getAddress() : null, bufLoc != null ? bufLoc.getAddress() : null));
        diffs.add(new FieldDiff("Location City", origLoc != null ? origLoc.getCity() : null, bufLoc != null ? bufLoc.getCity() : null));
        diffs.add(new FieldDiff("Location Country", origLoc != null ? origLoc.getCountry() : null, bufLoc != null ? bufLoc.getCountry() : null));
        diffs.add(new FieldDiff("Location Postal Code", origLoc != null ? origLoc.getPostalCode() : null, bufLoc != null ? bufLoc.getPostalCode() : null));
        diffs.add(new FieldDiff("Location Latitude", origLoc != null ? origLoc.getLatitude() : null, bufLoc != null ? bufLoc.getLatitude() : null));
        diffs.add(new FieldDiff("Location Longitude", origLoc != null ? origLoc.getLongitude() : null, bufLoc != null ? bufLoc.getLongitude() : null));

        Client origClient = originalCase != null ? originalCase.getClient() : null;
        Client bufClient = bufferedCase.getClient();
        diffs.add(new FieldDiff("Client ID", origClient != null ? origClient.getId() : null, bufClient != null ? bufClient.getId() : null));
        diffs.add(new FieldDiff("Client First Name", origClient != null ? origClient.getFirstName() : null, bufClient != null ? bufClient.getFirstName() : null));
        diffs.add(new FieldDiff("Client Last Name", origClient != null ? origClient.getLastName() : null, bufClient != null ? bufClient.getLastName() : null));
        diffs.add(new FieldDiff("Client Contact", origClient != null ? origClient.getContactNumber() : null, bufClient != null ? bufClient.getContactNumber() : null));

        Vehicle origVeh = originalCase != null ? originalCase.getClientVehicle() : null;
        Vehicle bufVeh = bufferedCase.getClientVehicle();
        diffs.add(new FieldDiff("Vehicle ID", origVeh != null ? origVeh.getId() : null, bufVeh != null ? bufVeh.getId() : null));
        diffs.add(new FieldDiff("Vehicle License Plate", origVeh != null ? origVeh.getLicensePlate() : null, bufVeh != null ? bufVeh.getLicensePlate() : null));
        diffs.add(new FieldDiff("Vehicle Model", origVeh != null ? origVeh.getModel() : null, bufVeh != null ? bufVeh.getModel() : null));
        diffs.add(new FieldDiff("Vehicle VIN", origVeh != null ? origVeh.getVin() : null, bufVeh != null ? bufVeh.getVin() : null));

        Operator origFirstOp = originalCase != null ? originalCase.getFirstOperator() : null;
        Operator bufFirstOp = bufferedCase.getFirstOperator();
        diffs.add(new FieldDiff("First Operator ID", origFirstOp != null ? origFirstOp.getId() : null, bufFirstOp != null ? bufFirstOp.getId() : null));
        diffs.add(new FieldDiff("First Operator Username", origFirstOp != null ? origFirstOp.getUsername() : null, bufFirstOp != null ? bufFirstOp.getUsername() : null));
        diffs.add(new FieldDiff("First Operator First Name", origFirstOp != null ? origFirstOp.getFirstName() : null, bufFirstOp != null ? bufFirstOp.getFirstName() : null));
        diffs.add(new FieldDiff("First Operator Last Name", origFirstOp != null ? origFirstOp.getLastName() : null, bufFirstOp != null ? bufFirstOp.getLastName() : null));

        Operator origLastOp = originalCase != null ? originalCase.getLastEditedOperator() : null;
        Operator bufLastOp = bufferedCase.getLastEditedOperator();
        diffs.add(new FieldDiff("Last Edit Operator ID", origLastOp != null ? origLastOp.getId() : null, bufLastOp != null ? bufLastOp.getId() : null));
        diffs.add(new FieldDiff("Last Edit Operator Username", origLastOp != null ? origLastOp.getUsername() : null, bufLastOp != null ? bufLastOp.getUsername() : null));
        diffs.add(new FieldDiff("Last Edit Operator First Name", origLastOp != null ? origLastOp.getFirstName() : null, bufLastOp != null ? bufLastOp.getFirstName() : null));
        diffs.add(new FieldDiff("Last Edit Operator Last Name", origLastOp != null ? origLastOp.getLastName() : null, bufLastOp != null ? bufLastOp.getLastName() : null));

        List<Note> origNotes = originalCase != null ? originalCase.getNotes() : null;
        List<Note> bufNotes = bufferedCase.getNotes();
        diffs.add(new FieldDiff("Notes Count", origNotes != null ? origNotes.size() : null, bufNotes != null ? bufNotes.size() : null));

        return diffs;
    }


    private String formatValue(Object value) {
        if (value == null) return "null";

        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else if (value instanceof LocalDate) {
            return ((LocalDate) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            return value.toString();
        }
    }
}