package com.revature.banking.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.util.Properties;

public class PropertiesLoader {
    private static Logger logger = LogManager.getLogger(PropertiesLoader.class);
    private static String adminUsername;
    private static String adminPassword;
    private static String endpoint;
    private static String dbUsername;
    private static String dbPassword;

    public static void load(String propertiesPath) {
        try {
            Properties properties = new Properties();
            FileReader fr = new FileReader(propertiesPath);
            properties.load(fr);
            adminUsername = properties.getProperty("adminUsername");
            adminPassword = properties.getProperty("adminPassword");
            endpoint = properties.getProperty("endpoint");
            dbUsername = properties.getProperty("dbUsername");
            dbPassword = properties.getProperty("dbPassword");
            fr.close();
        } catch (Exception e) {
            logger.error("failed to load properties file");
        }
    }

    public static String getAdminUsername() {
        return adminUsername;
    }

    public static String getAdminPassword() {
        return adminPassword;
    }

    public static String getEndpoint() {
        return endpoint;
    }

    public static String getDbUsername() {
        return dbUsername;
    }

    public static String getDbPassword() {
        return dbPassword;
    }
}
