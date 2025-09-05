package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.enums.BufferedChangeType;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.Method;
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

    private void displayCaseDiff(BufferableEntity<Long, Case> selectedData) {
        originalFieldDiffs.clear();
        modifiedFieldDiffs.clear();

        try {
            Case bufferedCase = selectedData.getEntity();
            Case originalCase = null;

            try {
                if (bufferedCase.getId() != null) {
                    CaseRepository caseRepository = new CaseRepository();
                    originalCase = caseRepository.findById(bufferedCase.getId());
                }
            } catch (Exception e) {
                if (e.getMessage().contains("recovery mode") ||
                        e.getMessage().contains("connection") ||
                        e.getCause() != null && e.getCause().getMessage().contains("FATAL")) {
                }
            }

            List<FieldDiff> diffs = generateAllFieldDiffs(originalCase, bufferedCase);

            for (FieldDiff diff : diffs) {
                originalFieldDiffs.add(diff);
                modifiedFieldDiffs.add(diff);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<FieldDiff> generateAllFieldDiffs(Case originalCase, Case bufferedCase) {
        List<FieldDiff> diffs = new ArrayList<>();

        if (originalCase != null) {
            // Case
            diffs.add(new FieldDiff("ID", originalCase.getId(), bufferedCase.getId()));
            diffs.add(new FieldDiff("Damage Description", originalCase.getDamageDescription(), bufferedCase.getDamageDescription()));
            diffs.add(new FieldDiff("Case State", originalCase.getState(), bufferedCase.getState()));
            diffs.add(new FieldDiff("Damage Type", originalCase.getDamageType(), bufferedCase.getDamageType()));
            diffs.add(new FieldDiff("Damage Cause", originalCase.getDamageCause(), bufferedCase.getDamageCause()));
            diffs.add(new FieldDiff("Created Date", originalCase.getCreatedDateTime(), bufferedCase.getCreatedDateTime()));
            diffs.add(new FieldDiff("Vehicle Registration Date", originalCase.getClientVehicleFirstRegistrationDate(), bufferedCase.getClientVehicleFirstRegistrationDate()));

            Location origLoc = originalCase.getLocation();
            if(origLoc != null){
                Location bufLoc = bufferedCase.getLocation();
                diffs.add(new FieldDiff("Location ID", origLoc != null ? origLoc.getId() : null, bufLoc != null ? bufLoc.getId() : null));
                diffs.add(new FieldDiff("Location Address", origLoc.getAddress(), bufLoc.getAddress()));
                diffs.add(new FieldDiff("Location City", origLoc.getCity(), bufLoc.getCity()));
                diffs.add(new FieldDiff("Location Country", origLoc.getCountry(), bufLoc.getCountry()));
                diffs.add(new FieldDiff("Location Postal Code", origLoc.getPostalCode(), bufLoc.getPostalCode()));
                diffs.add(new FieldDiff("Location Latitude", origLoc.getLatitude(), bufLoc.getLatitude()));
                diffs.add(new FieldDiff("Location Longitude", origLoc.getLongitude(), bufLoc.getLongitude()));
            }

            // Client
            Client origClient = originalCase.getClient();
            Client bufClient = bufferedCase.getClient();
            if(origClient != null && bufClient != null){
                diffs.add(new FieldDiff("Client ID",origClient.getId(bufClient != null ? bufClient.getId() : null)));
                diffs.add(new FieldDiff("Client First Name", origClient.getFirstName(), bufClient.getFirstName()));
                diffs.add(new FieldDiff("Client Last Name", origClient.getLastName(), bufClient.getLastName()));
                diffs.add(new FieldDiff("Client Contact", origClient.getContactNumber(), bufClient.getContactNumber()));
            }
            // Vehicle
            Vehicle origVeh = originalCase.getClientVehicle();
            Vehicle bufVeh = bufferedCase.getClientVehicle();
            diffs.add(new FieldDiff("Vehicle ID", origVeh != null ? origVeh.getId() : null, bufVeh != null ? bufVeh.getId() : null));
            diffs.add(new FieldDiff("Vehicle License Plate", origVeh.getLicensePlate(), bufVeh.getLicensePlate()));
            diffs.add(new FieldDiff("Vehicle Model", origVeh.getModel(), bufVeh.getModel()));
            diffs.add(new FieldDiff("Vehicle VIN", origVeh.getVin(), bufVeh.getVin()));

            // First Operator
            Operator origFirstOp = originalCase.getFirstOperator();
            Operator bufFirstOp = bufferedCase.getFirstOperator();
            diffs.add(new FieldDiff("First Operator ID", origFirstOp != null ? origFirstOp.getId() : null, bufFirstOp != null ? bufFirstOp.getId() : null));
            diffs.add(new FieldDiff("First Operator Username", origFirstOp.getUsername(), bufFirstOp.getUsername()));
            diffs.add(new FieldDiff("First Operator First Name", origFirstOp.getFirstName(), bufFirstOp.getFirstName()));
            diffs.add(new FieldDiff("First Operator Last Name", origFirstOp.getLastName(), bufFirstOp.getLastName()));

            // Last Operator
            Operator origLastOp = originalCase.getLastEditedOperator();
            Operator bufLastOp = bufferedCase.getLastEditedOperator();
            diffs.add(new FieldDiff("Last Edit Operator ID", origLastOp != null ? origLastOp.getId() : null, bufLastOp != null ? bufLastOp.getId() : null));
            diffs.add(new FieldDiff("Last Edit Operator Username", origLastOp.getUsername(), bufLastOp.getUsername()));
            diffs.add(new FieldDiff("Last Edit Operator First Name", origLastOp.getFirstName(), bufLastOp.getFirstName()));
            diffs.add(new FieldDiff("Last Edit Operator Last Name", origLastOp.getLastName(), bufLastOp.getLastName()));

            // Notes
            List<Note> origNotes = originalCase.getNotes();
            List<Note> bufNotes = bufferedCase.getNotes();
            diffs.add(new FieldDiff("Notes Count", origNotes != null ? origNotes.size() : null, bufNotes != null ? bufNotes.size() : null));
        }

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