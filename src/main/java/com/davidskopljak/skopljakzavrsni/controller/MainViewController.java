    package com.davidskopljak.skopljakzavrsni.controller;

    import com.davidskopljak.skopljakzavrsni.entity.*;
    import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
    import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
    import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.scene.control.ComboBox;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.TextArea;
    import javafx.scene.input.KeyCode;
    import javafx.scene.input.KeyEvent;
    import javafx.stage.Stage;

    import java.util.ArrayList;
    import java.util.List;

    public class MainViewController {
        @FXML
        private TableView<Case> casesTableView;

        @FXML
        private TableColumn<Case, Long> caseIdTableColumn;

        @FXML
        private TableColumn<Case, String> lastNameTableColumn;

        @FXML
        private TableColumn<Case, String> firstNameTableColumn;

        @FXML
        private TableColumn<Case, String> vehicleBrandTableColumn;

        @FXML
        private TableColumn<Case, String> licensePlateTableColumn;

        @FXML
        private TableColumn<Case, String> locationTableColumn;

        @FXML
        private TableColumn<Case, String> firstEditedOperatorTableColumn;

        @FXML
        private TableColumn<Case, String> lastEditedOperatorTableColumn;

        @FXML
        private TextArea filterTextArea;

        @FXML
        private ComboBox<String> filterComboBox;

        // Observable lists for data management
        private ObservableList<Case> allCases = FXCollections.observableArrayList();
        private ObservableList<Case> filteredCases = FXCollections.observableArrayList();

        List<Stage> openCases = new ArrayList<>();

        public void initialize() {
            filterComboBox.getItems().addAll(
                    "Case ID",
                    "Last name",
                    "First name",
                    "Vehicle brand",
                    "License plate",
                    "Location",
                    "First edited operator",
                    "Last edited operator"
            );

            filterTextArea.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
                if (ke.getCode() == KeyCode.ENTER) {
                    ke.consume();
                    MiscHelpers.runThread(() -> {
                        try {
                            String selectedColumn = filterComboBox.getSelectionModel().getSelectedItem();
                            String filter = filterTextArea.getText().trim().toLowerCase();

                            final List<Case> results;
                            if (selectedColumn == null) {
                                results = matchAnyColumn(new ArrayList<>(allCases), filter);
                            } else {
                                results = matchColumn(new ArrayList<>(allCases), filter, selectedColumn);
                            }

                            javafx.application.Platform.runLater(() -> {
                                filteredCases.setAll(results);
                                casesTableView.sort();
                            });
                        } catch (Exception e) {
                            CRMApplication.log.error("Search failed", e);
                        }
                    });
                }
            });

            initializeCaseTable();

            try{
                CaseRepository caseRepository = new CaseRepository();
                List<Case> cases = caseRepository.findAll();
                allCases.setAll(cases);
                filteredCases.setAll(cases);
            } catch (Exception e) {
                throw new RepositoryAccessException("Failed to load cases: " + e);
            }
        }

        private void initializeCaseTable() {
            // Set the observable list as the table's items
            casesTableView.setItems(filteredCases);

            caseIdTableColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleLongProperty(cellData.getValue().getId()).asObject());

            lastNameTableColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClient().getLastName()));

            firstNameTableColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClient().getFirstName()));

            vehicleBrandTableColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClientVehicle().getModel().toString()));

            locationTableColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLocation().getAddress() + ", " + cellData.getValue().getLocation().getCity()));

            licensePlateTableColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClientVehicle().getLicensePlate()));

            firstEditedOperatorTableColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFirstOperator().getFirstName() + " " + cellData.getValue().getFirstOperator().getLastName()));

            lastEditedOperatorTableColumn.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLastEditedOperator().getFirstName() + " " + cellData.getValue().getLastEditedOperator().getLastName()));

            caseIdTableColumn.setSortType(TableColumn.SortType.DESCENDING);
            casesTableView.getSortOrder().add(caseIdTableColumn);
            casesTableView.sort();

            casesTableView.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    Case selectedCase = casesTableView.getSelectionModel().getSelectedItem();
                    if (selectedCase != null) {
                        try {
                            CaseWindowController caseWindowController = new CaseWindowController(selectedCase);
                            openCases.add(caseWindowController.getStage());
                        } catch (Exception e) {
                            CRMApplication.log.error("Failed to open existing case window: ", e);
                        }
                    }
                }
            });
        }

        private List<Case> matchAnyColumn(List<Case> allCases, String filter){
            return allCases.stream()
                    .filter(c -> String.valueOf(c.getId()).contains(filter)
                            || c.getClient().getLastName().toLowerCase().contains(filter)
                            || c.getClient().getFirstName().toLowerCase().contains(filter)
                            || c.getClientVehicle().getModel().toString().toLowerCase().contains(filter)
                            || c.getClientVehicle().getLicensePlate().toLowerCase().contains(filter)
                            || (c.getLocation().getAddress() + ", " + c.getLocation().getCity()).toLowerCase().contains(filter)
                            || (c.getFirstOperator().getFirstName() + " " + c.getFirstOperator().getLastName()).toLowerCase().contains(filter)
                            || (c.getLastEditedOperator().getFirstName() + " " + c.getLastEditedOperator().getLastName()).toLowerCase().contains(filter))
                    .toList();
        }

        private List<Case> matchColumn(List<Case> cases, String filter, String column) {
            java.util.function.Function<Case, String> columnExtractor;

            switch (column) {
                case "Case ID" -> columnExtractor = c -> String.valueOf(c.getId());
                case "Last name" -> columnExtractor = c -> c.getClient().getLastName();
                case "First name" -> columnExtractor = c -> c.getClient().getFirstName();
                case "Vehicle brand" -> columnExtractor = c -> c.getClientVehicle().getModel().toString();
                case "License plate" -> columnExtractor = c -> c.getClientVehicle().getLicensePlate();
                case "Location" -> columnExtractor = c -> c.getLocation().getAddress() + ", " + c.getLocation().getCity();
                case "First edited operator" -> columnExtractor = c -> c.getFirstOperator().getFirstName() + " " + c.getFirstOperator().getLastName();
                case "Last edited operator" -> columnExtractor = c -> c.getLastEditedOperator().getFirstName() + " " + c.getLastEditedOperator().getLastName();
                default -> columnExtractor = c -> "";
            }

            return cases.stream()
                    .filter(c -> {
                        String value = columnExtractor.apply(c);
                        return value != null && value.toLowerCase().contains(filter);
                    })
                    .toList();
        }
    }