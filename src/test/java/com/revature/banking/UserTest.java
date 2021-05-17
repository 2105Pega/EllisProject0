package com.revature.banking;

import com.revature.banking.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {
    class ConcreteUser extends User {
        ConcreteUser(String username, String passwordHash) {
            super(username, passwordHash);
        }
    }

    @Test
    public void UserConstructorTest() {
        ConcreteUser u = new ConcreteUser("username", "password");
        Assertions.assertEquals(u.getUsername(), "username");
        Assertions.assertEquals(u.getPasswordHash(), "password");
    }

    @Test
    public void getUsernameTest() {
        ConcreteUser u = new ConcreteUser("username", "password");
        Assertions.assertEquals(u.getUsername(), "username");
    }

    @Test
    public void setUsernameTest() {
        ConcreteUser u = new ConcreteUser("username", "password");
        u.setUsername("newname");
        Assertions.assertEquals(u.getUsername(), "newname");
    }

    @Test
    public void getPasswordHashTest() {
        ConcreteUser u = new ConcreteUser("username", "password");
        Assertions.assertEquals(u.getPasswordHash(), "password");
    }

    @Test
    public void setPasswordHashTest() {
        ConcreteUser u = new ConcreteUser("username", "password");
        u.setPasswordHash("newhash");
        Assertions.assertEquals(u.getPasswordHash(), "newhash");
    }
}
