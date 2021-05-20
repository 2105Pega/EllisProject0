package com.revature.banking.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.util.Properties;

public class PropertiesLoader {
    private static Logger logger = LogManager.getLogger(PropertiesLoader.class);
    private String adminUsername;
    private String adminPassword;
    private String endpoint;

    public void load(String propertiesPath) {
        try {
            Properties properties = new Properties();
            FileReader fr = new FileReader(propertiesPath);
            properties.load(fr);
            adminUsername = properties.getProperty("adminUsername");
            adminPassword = properties.getProperty("adminPassword");
            endpoint = properties.getProperty("endpoint");
            fr.close();
        } catch (Exception e) {
            logger.error("failed to load properties file");
        }
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getEndpoint() {
        return endpoint;
    }
}