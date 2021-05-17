package com.revature.banking.dao;

import com.revature.banking.models.Deposit;
import com.revature.banking.models.Withdraw;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class TransactionDaoTest {
    TransactionDao transactionDao;
    Withdraw transaction;
    @BeforeEach
    void setUp() {
        transactionDao = new TransactionDao();
        transaction = new Withdraw(500, UUID.randomUUID());
        transactionDao.add(transaction);
    }

    @Test
    void getAll() {
        Deposit transaction2 = new Deposit(500, UUID.randomUUID());
        transactionDao.add(transaction2);
        Assertions.assertEquals(transactionDao.getAll().size(), 2);
    }

    @Test
    void get() {
        Assertions.assertEquals(transaction, transactionDao.get(transaction.getId()));
    }

    @Test
    void add() {
        Deposit transaction2 = new Deposit(500, UUID.randomUUID());
        transactionDao.add(transaction2);
        Assertions.assertEquals(transaction2, transactionDao.get(transaction2.getId()));
    }
}