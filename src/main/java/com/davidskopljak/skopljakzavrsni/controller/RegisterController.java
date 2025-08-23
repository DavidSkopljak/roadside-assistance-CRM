package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Operator;
import com.davidskopljak.skopljakzavrsni.exceptions.AccountCreationExcepiton;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.repository.OperatorRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

public class RegisterController {
    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField confirmPasswordTextField;

    // NOTE: nek se zna da sam pokusao ovdje maknuti @FXML jer je metoda ali se aplikacija ne pali ako ga tu ne stavim ¯\_(ツ)_/¯
    @FXML
    private void handleRegister(ActionEvent event) {
        String firstName = firstNameTextField.getText().trim();
        String lastName = lastNameTextField.getText().trim();
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText();
        String confirm = confirmPasswordTextField.getText();

        Path existingUsersPath = Path.of("users.txt");

        if (username.isEmpty() || password.isEmpty()) {
            MiscHelpers.showAlert("Username and password must not be empty.");
            return;
        }
        if (!password.equals(confirm)) {
            MiscHelpers.showAlert("Passwords do not match.");
            return;
        }

        try{
            if (Files.exists(existingUsersPath)) {
                List<String> lines = Files.readAllLines(existingUsersPath);
                for (int i = 0; i < lines.size(); i += 2) {
                    if (lines.get(i).equalsIgnoreCase(username)) {
                        MiscHelpers.showAlert("Username already exists. Choose another.");
                        return;
                    }
                }
            }
        } catch (IOException e){
            throw new AccountCreationExcepiton("Could not read existing users: " + e.getMessage());
        }


        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            String base64Hash = Base64.getEncoder().encodeToString(hashBytes);

            saveAccount(username, base64Hash, firstName, lastName);

            MiscHelpers.showAlert("Registration successful!", Alert.AlertType.INFORMATION);

            usernameTextField.clear();
            passwordTextField.clear();
            confirmPasswordTextField.clear();
            firstNameTextField.clear();
            lastNameTextField.clear();

        } catch (Exception e) {
            MiscHelpers.showAlert("Could not save user: " + e.getMessage());
        }
    }

    private static void saveAccount(String username, String base64Hash, String firstName, String lastName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt", true))) {
            writer.println(username.toLowerCase());
            writer.println(base64Hash);
            writer.println(MiscHelpers.capitalize(firstName));
            writer.println(MiscHelpers.capitalize(lastName));

            OperatorRepository repository = new OperatorRepository();
            Long operatorId = repository.save(new Operator(username, firstName, lastName));
            CRMApplication.logIn(new Operator(operatorId, username, firstName, lastName));
            MiscHelpers.loadScene("main-view.fxml", "Cases overview", CRMApplication.getPrimaryStage());
        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
            throw new AccountCreationExcepiton("Failed to create new account.");
        }
    }

    @FXML
    public void handleOpenLogin(ActionEvent event) {
        MiscHelpers.loadScene("login.fxml", "Log in", CRMApplication.getPrimaryStage());
    }
}
