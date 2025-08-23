package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.exceptions.AccountLoginException;
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
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LoginController {
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    // NOTE: nek se zna da sam pokusao ovdje maknuti @FXML jer je metoda ali se aplikacija ne pali ako ga tu ne stavim ¯\_(ツ)_/¯
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameTextField.getText().trim();
        String password = passwordPasswordField.getText();

        Path existingUsersPath = Path.of("users.txt");

        if (username.isEmpty() || password.isEmpty()) {
            MiscHelpers.showAlert("Username and password must not be empty.");
            return;
        }

        try {
            if (!Files.exists(existingUsersPath)) {
                MiscHelpers.showAlert("No users are registered yet.");
                return;
            }

            String currentHash = null;
            String firstName = null;
            String lastName = null;

            try (BufferedReader reader = new BufferedReader(new FileReader(existingUsersPath.toFile()))) {
                String storedUsername;
                while ((storedUsername = reader.readLine()) != null) {
                    String storedHash = reader.readLine();
                    String storedFirstName = reader.readLine();
                    String storedLastName = reader.readLine();

                    if (storedUsername.equalsIgnoreCase(username)) {
                        currentHash = storedHash;
                        firstName = storedFirstName;
                        lastName = storedLastName;
                        break;
                    }
                }

                if (currentHash == null) {
                    MiscHelpers.showAlert("Incorrect username or password. Please try again.");
                    return;
                }
            }

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String base64Hash = Base64.getEncoder().encodeToString(hashBytes);

            if (currentHash.equals(base64Hash)) {
                MiscHelpers.showAlert("Login successful!", Alert.AlertType.INFORMATION);

                System.out.println("Logged in user: " + username + ", First Name: " + firstName + ", Last Name: " + lastName);

                usernameTextField.clear();
                passwordPasswordField.clear();

                OperatorRepository repository = new OperatorRepository();
                CRMApplication.logIn(repository.findByUsername(username));
                MiscHelpers.loadScene("main-view.fxml", "Cases overview", CRMApplication.getPrimaryStage());

            } else {
                MiscHelpers.showAlert("Incorrect username or password. Please try again.");
            }

        } catch (NoSuchAlgorithmException e) {
            throw new AccountLoginException("Error with SHA256 algorithm: " + e.getMessage());
        } catch (IOException e) {
            throw new AccountLoginException("Could not read existing users: " + e.getMessage());
        }
    }

    @FXML
    public void handleOpenRegister(ActionEvent event) {
        MiscHelpers.loadScene("register.fxml", "Register", CRMApplication.getPrimaryStage());
    }
}
