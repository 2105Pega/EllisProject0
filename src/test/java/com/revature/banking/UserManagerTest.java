package com.revature.banking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

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

    @Test
    void hashPassword() throws Exception {
        String password = "password";
        Method hashPassword = UserManager.class.getDeclaredMethod("hashPassword", String.class);
        hashPassword.setAccessible(true);
        String passwordHash = (String) hashPassword.invoke(um, password);
        String correctHash = "5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8";
        assertEquals(passwordHash, correctHash);
    }

    @Test
    void bytesToHex() throws Exception {
        Method bytesToHex = UserManager.class.getDeclaredMethod("bytesToHex", byte[].class);
        bytesToHex.setAccessible(true);

        byte bytes[] = { (byte) 0xAB, (byte) 0xCD, (byte)0xEF };
        String s = (String) bytesToHex.invoke(um, bytes);
        assertEquals(s, "ABCDEF");
    }

    @Test
    void nibbleToChar() throws Exception {
        Method nibbleToChar = UserManager.class.getDeclaredMethod("nibbleToChar", byte.class);
        nibbleToChar.setAccessible(true);

        byte nibble = (byte) 0x5;
        byte nibble2 = (byte) 0xF;

        assertEquals((Character) nibbleToChar.invoke(um, nibble), '5');
        assertEquals((Character) nibbleToChar.invoke(um, nibble2), 'F');
    }
}