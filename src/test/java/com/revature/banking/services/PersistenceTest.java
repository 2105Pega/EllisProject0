package com.revature.banking.services;

import com.revature.banking.models.*;
import com.revature.banking.services.Persistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PersistenceTest {
    Persistence p;
    Client alice;
    Employee bob;
    Account account;
    Account account2;
    Transaction transaction;
    Transaction transaction2;

    @BeforeEach
    void setUp() {
        p = new Persistence();
        alice = new Client("alice", "***");
        bob = new Employee("bob", "***");
        p.addUser(alice);
        p.addUser(bob);
        account = new Account("alice", "checking");
        account2 = new Account("bob", "savings");
        p.addAccount(account);
        p.addAccount(account2);
        transaction = new Deposit(500, account.getUuid());
        transaction2 = new Withdraw(500, account.getUuid());
        p.addTransaction(transaction);
        p.addTransaction(transaction2);
    }

    @Test
    void writeToFile() {
        Assertions.assertDoesNotThrow( () -> { p.writeToFile("src/main/resources/testfile.txt"); } );
    }

    @Test
    void getUsers() {
        ArrayList<User> users = p.getUsers();
        Assertions.assertEquals(users.contains(alice), true);
        Assertions.assertEquals(users.contains(bob), true);
    }

    @Test
    void getUser() {
        Assertions.assertEquals(p.getUser("alice"), alice);
        Assertions.assertEquals(p.getUser("bob"), bob);
    }

    @Test
    void addUser() {
        Client c = new Client("jeff", "***");
        p.addUser(c);
        Assertions.assertEquals(p.getUser("jeff"), c);
    }

    @Test
    void getAccounts() {
        Assertions.assertEquals(p.getAccounts().size(), 2);
    }

    @Test
    void getAccountByUUID() {
        Assertions.assertEquals(p.getAccount(account.getUuid()), account);
    }

    @Test
    void getAccountByUser() {
        Assertions.assertEquals(p.getAccount("alice", "checking"), account);
    }

    @Test
    void addAccount() {
        Account account3 = new Account("alice", "savings");
        p.addAccount(account3);
        Assertions.assertEquals(p.getAccount("alice", "savings"), account3);
    }

    @Test
    void getTransactions() {
        Assertions.assertEquals(p.getTransactions().size(), 2);
    }

    @Test
    void getTransaction() {
        Assertions.assertEquals(p.getTransaction(transaction.getId()), transaction);
    }

    @Test
    void addTransaction() {
        Transaction transaction3 = new Transfer(500, account.getUuid(), account2.getUuid());
        p.addTransaction(transaction3);
        Assertions.assertEquals(p.getTransaction(transaction3.getId()), transaction3);
    }
}