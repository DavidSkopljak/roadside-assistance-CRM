package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.controller.CRMApplication;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private static DatabaseConnectionManager instance;
    private static HikariDataSource dataSource;

    private DatabaseConnectionManager() {
        try(FileReader propsFileReader = new FileReader("database.properties")) {
            HikariConfig config = new HikariConfig();
            Properties props = new Properties();
            props.load(propsFileReader);

            config.setJdbcUrl(props.getProperty("databaseUrl"));
            config.setUsername(props.getProperty("username"));
            config.setPassword(props.getProperty("password"));
            config.setMaximumPoolSize(10);
            config.setConnectionTimeout(30000);

            setDataSource(new HikariDataSource(config));
        } catch (IOException e) {
            throw new RepositoryAccessException("Podaci za spajanje s bazom nisu dostupni", e);
        }
    }

    private static void setDataSource(HikariDataSource dataSource) {
        DatabaseConnectionManager.dataSource = dataSource;
    }

    public static synchronized DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    // Get a connection from the pool
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Optional: Close the connection pool when application shuts down
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
