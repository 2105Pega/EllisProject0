package com.revature.banking.models;

import org.junit.jupiter.api.*;

public class AccountTest {
    Account account;
    @BeforeEach
    void setup() {
        account = new Account("username", "accountName");
    }

    @Test
    void getBalance() {
        account.setBalance(500);
        Assertions.assertEquals(account.getBalance(), 500);
    }

    @Test
    void setBalance() {
        account.setBalance(500);
        Assertions.assertEquals(account.getBalance(), 500);
    }

    @Test
    void addAccountHolder() {
        account.addAccountHolder("username2");
        Assertions.assertEquals(account.getAccountHolders().contains("username2"), true);
    }

    @Test
    void getAccountHolders() {
        account.addAccountHolder("username2");
        Assertions.assertEquals(account.getAccountHolders().contains("username"), true);
        Assertions.assertEquals(account.getAccountHolders().contains("username2"), true);
    }

    @Test
    void getName() {
        Assertions.assertEquals(account.getName(), "accountName");
    }

    @Test
    void getUuid() {
    }

    @Test
    void getStatus() {
        Assertions.assertEquals(account.getStatus(), Account.Status.PENDING);
    }

    @Test
    void setStatus() {
        account.setStatus(Account.Status.APPROVED);
        Assertions.assertEquals(account.getStatus(), Account.Status.APPROVED);
    }
}
