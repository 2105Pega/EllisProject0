package com.revature.banking.dao;

import com.revature.banking.models.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class TransactionDaoTest {
    TransactionDao transactionDao;
    Transaction transaction;
    @BeforeEach
    void setUp() {
        transactionDao = new TransactionDao();
        transaction = new Transaction(500, 1, Transaction.Type.DEPOSIT);
        transactionDao.add(transaction);
    }

    @Test
    void getAll() {
        Transaction transaction2 = new Transaction(500, 1, Transaction.Type.DEPOSIT);
        transactionDao.add(transaction2);
        Assertions.assertEquals(transactionDao.getAll().size(), 2);
    }

    @Test
    void get() {
        Assertions.assertEquals(transaction, transactionDao.get(transaction.getId()));
    }

    @Test
    void add() {
        Transaction transaction2 = new Transaction(500, 1, Transaction.Type.DEPOSIT);
        transactionDao.add(transaction2);
        Assertions.assertEquals(transaction2, transactionDao.get(transaction2.getId()));
    }
}