package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.controller.CRMApplication;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private static DatabaseConnectionManager instance = null;
    private static Connection connection = null;
    private static Boolean connectionInUse= false;

    private DatabaseConnectionManager() {}

    public static Boolean connectionInUse() {
        return connectionInUse;
    }

    public static void setConnectionInUse(Boolean status) {
        connectionInUse = status;
    }

    public static synchronized DatabaseConnectionManager getInstance() {
        try {
            if (instance == null) {
                instance = new DatabaseConnectionManager();
                connection = instance.connectToDatabase();
            }
            return instance;
        } catch (SQLException e) {
            throw new RepositoryAccessException(e);
        }

    }

    private Connection connectToDatabase() throws SQLException {
        try(FileReader fileReader = new FileReader("database.properties")) {
            Properties props = new Properties();
            props.load(fileReader);
            String url = props.getProperty("databaseUrl");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
            System.out.println(url + ":" + username + ":" + password);
            return (DriverManager.getConnection(
                    props.getProperty("databaseUrl"),
                    props.getProperty("username"),
                    props.getProperty("password")));
        } catch (IOException | SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RepositoryAccessException(e);
        }
    }
}
