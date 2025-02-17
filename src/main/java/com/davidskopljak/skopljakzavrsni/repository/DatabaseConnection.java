package com.davidskopljak.skopljakzavrsni.repository;
import com.davidskopljak.skopljakzavrsni.entity.Client;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    public static void main(String[] args) throws IOException {
    // Call the connect method

    // Close the connection (optional in this example)
        /*Properties props = new Properties();
        props.load(new FileReader("database.properties"));
        String url = props.getProperty("databaseUrl");
        String user = props.getProperty("username");
        String password = props.getProperty("password");
        System.out.println(url + ":" + user + ":" + password);*/
        ClientRepository clientRepository = new ClientRepository();
        try{
            Long clientId = clientRepository.save(new Client("test", "test", "123"));
            System.out.println("client with id: " + clientId + " saved to database");
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}/*
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/crm";
    private static final String USER = "crmadmin";
    private static final String PASSWORD = "root";

    public static void main(String[] args) throws IOException, SQLException {
        Properties props = new Properties();
        props.load(new FileReader("database.properties"));
        String url = props.getProperty("databaseUrl");
        String user = props.getProperty("username");
        String password = props.getProperty("password");
        try (Connection connection = DriverManager.getConnection(url, user, password);) {
            System.out.println("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}*/