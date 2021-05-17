package com.revature.banking.dao;

import com.revature.banking.models.Client;
import com.revature.banking.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    UserDao userDao;
    @BeforeEach
    void setUp() {
        userDao = new UserDao();
    }

    @Test
    void getAll() {
        userDao.add(new Client("bob", "***"));
        userDao.add(new Client("alice", "***"));
        assertEquals(userDao.getAll().size(), 2);
    }

    @Test
    void get() {
        User alice = new Client("alice", "***");
        userDao.add(alice);
        assertEquals(userDao.get(alice.getUsername()), alice);
    }

    @Test
    void add() {
        User alice = new Client("alice", "***");
        userDao.add(alice);
        assertEquals(userDao.get(alice.getUsername()), alice);
    }
}