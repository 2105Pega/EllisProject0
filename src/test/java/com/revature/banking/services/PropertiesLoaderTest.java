package com.revature.banking.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesLoaderTest {
    static PropertiesLoader p;

    @BeforeAll
    static void load() {
        p = new PropertiesLoader();
        p.load("src/main/resources/test.properties");
    }

    @Test
    void getAdminUsername() {
        Assertions.assertEquals("admin", p.getAdminUsername());
    }

    @Test
    void getAdminPassword() {
        Assertions.assertEquals("password", p.getAdminPassword());
    }

    @Test
    void getEndpoint() {
        Assertions.assertEquals("localhost", p.getEndpoint());
    }
}