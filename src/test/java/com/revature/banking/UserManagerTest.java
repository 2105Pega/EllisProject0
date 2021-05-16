package com.revature.banking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {
    Persistence p;
    UserManager um;
    @BeforeEach
    void setUp() {
        p = new Persistence();
        um = new UserManager(p);
    }

    @Test
    void createClient() {
        um.createClient("bob", "password");
        Assertions.assertNotEquals(um.getUser("bob"), null);
    }

    @Test
    void createEmployee() {
        um.createClient("bob", "password");
        Assertions.assertNotEquals(um.getUser("bob"), null);
    }

    @Test
    void getUser() {
        um.createClient("bob", "password");
        Assertions.assertNotEquals(um.getUser("bob"), null);
    }

    @Test
    void verifyPassword() {
        um.createClient("bob", "password");
        Assertions.assertEquals(um.verifyPassword("bob", "password"), true);
        Assertions.assertEquals(um.verifyPassword("bob", "wrong"), false);
    }

    @Test
    void userExists() {
        um.createClient("bob", "password");
        Assertions.assertEquals(um.userExists("bob"), true);
    }

    @Test
    void clientExists() {
        um.createClient("bob", "password");
        um.createEmployee("alice", "password");
        Assertions.assertEquals(um.clientExists("bob"), true);
        Assertions.assertEquals(um.clientExists("alice"), false);
    }

    @Test
    void getAccountNames() {
        um.createClient("bob", "password");
        Assertions.assertEquals(um.getAccountNames("bob").size(), 0);
    }
}