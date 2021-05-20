package com.revature.banking.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    static Logger logger = LogManager.getLogger(ConnectionManager.class);

    public static Connection getConnection(PropertiesLoader p) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
           logger.error("database driver class not found");
        }
        String url = "jdbc:postgresql://" + p.getEndpoint();
        String username = p.getDbUsername();
        String password = p.getDbPassword();
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.error("failed to get database connection");
        }
        return null;
    }
}
