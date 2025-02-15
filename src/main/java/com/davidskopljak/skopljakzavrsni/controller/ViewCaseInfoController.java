package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageCause;
import com.davidskopljak.skopljakzavrsni.enums.VehicleDamageType;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

public class ViewCaseInfoController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField contactNumber;
    @FXML
    private TextField licensePlate;
    @FXML
    private TextField vin;
    @FXML
    private DatePicker firstRegDate;
    @FXML
    private ComboBox<VehicleModel> model;
    @FXML
    private ComboBox<VehicleDamageType> damageType;
    @FXML
    private ComboBox<VehicleDamageCause> damageCause;
    @FXML
    private TextField damageDescription;

    public void initialize() {
        ObservableList<VehicleModel> vehicleModels = FXCollections.observableArrayList(VehicleModel.values());
        model.getItems().setAll(vehicleModels);

        ObservableList<VehicleDamageType> damageTypes = FXCollections.observableArrayList(VehicleDamageType.values());
        damageType.getItems().setAll(damageTypes);

        ObservableList<VehicleDamageCause> damageCauses = FXCollections.observableArrayList(VehicleDamageCause.values());
        damageCause.getItems().setAll(damageCauses);


    }

    public void extractTextFromField(String id, TextField field) {
        System.out.println("Function called from text field: " + id);
        System.out.println(field.getText());
    }

    public void extractTextFromComboBox(String id, ComboBox comboBox) {
        System.out.println("Function called from comboBox: " + id);
        System.out.println(comboBox.getValue());
    }

}
