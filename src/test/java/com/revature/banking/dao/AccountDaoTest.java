package com.revature.banking.dao;

import com.revature.banking.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountDaoTest {
    AccountDao accountDao;
    @BeforeEach
    void setUp() {
        accountDao = new AccountDao();
    }

    @Test
    void getAll() {
        accountDao.add(new Account("bob", "savings"));
        accountDao.add(new Account("alice", "savings"));
        assertEquals(accountDao.getAll().size(), 2);
    }

    @Test
    void get() {
        Account account = new Account("alice", "savings");
        accountDao.add(account);
        assertEquals(accountDao.get(1, "savings"), account);
    }

    @Test
    void add() {
        Account account = new Account("alice", "savings");
        accountDao.add(account);
        assertEquals(accountDao.get(1, "savings"), account);
    }
}