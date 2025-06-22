package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import javafx.event.ActionEvent;

public class NewCaseMenuController {
    public void handleViewCaseInfo(){
        MiscHelpers.loadScene("new-case.fxml", "View case info");
    }

    public void handleViewLocationInfo() {
        MiscHelpers.loadScene("new-case-location.fxml", "View location");
    }

    public void handleSaveCase() {

    }

    public void handleCancelCase(ActionEvent actionEvent) {
    }

    /*public void saveCase(){
        try{
            validateCaseInfo();
            //validateLocationInfo();
            Location location = new Location("address", "city", "country", "postalCode", new BigDecimal(45.8089772239981), new BigDecimal(15.716704654770382));
            Client client = new Client(firstName, lastName, contactNumber);
            Vehicle clientVehicle = new Vehicle(licensePlate, vehicleModel, vinText);
            Operator firstOperator = CRMApplication.getActiveOperator();
            CaseState caseState = CaseState.ACTIVE;

            Case newCase = new Case();

            newCase.setClient(client)
                    .setClientVehicle(clientVehicle)
                    .setClientVehicleFirstRegistrationDate(LocalDate.now().minusDays(10))
                    .setDamageCause(damageCause)
                    .setDamageType(damageType)
                    .setFirstOperator(firstOperator)
                    .setLastEditedOperator(firstOperator)
                    .setLocation(location)
                    .setDamageDescription(damageDescription)
                    .setCreatedDateTime(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));

            newCase.updateState(caseState);

            CaseRepository caseRepository = new CaseRepository();
            caseRepository.save(newCase);

        }catch (InvalidCaseInfoException e){
            CRMApplication.log.error(e.getMessage());
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText(null);
            a.setContentText("Invalid case information. Please check the case details.");
            a.show();
        } catch (SQLException e) {
            throw new RepositoryAccessException("Failed to save new case: " + e);
        }
    }*/
}
