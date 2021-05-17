package com.revature.banking.services;

import com.revature.banking.models.Account;
import com.revature.banking.models.Client;
import com.revature.banking.services.AccountManager;
import com.revature.banking.services.Persistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountManagerTest {
    AccountManager am;
    Persistence p;

    @BeforeEach
    void setup() {
        p = new Persistence();
        am = new AccountManager(p);
        p.addUser(new Client("alice", "***"));
        p.addUser(new Client("bob", "***"));
    }

    @Test
    void createAccount() {
        am.createAccount("alice", "savings");
        Account account = p.getAccount("alice", "savings");
        Assertions.assertNotEquals(account, null);
    }

    @Test
    void addUser() {
        am.createAccount("alice", "savings");
        Account account = p.getAccount("alice", "savings");
        am.addUser("bob", account.getUuid());
        Assertions.assertNotEquals(p.getAccount("bob", "savings"), null);
    }

    @Test
    void approveAccount() {
        am.createAccount("alice", "savings");
        Account account = p.getAccount("alice", "savings");
        am.approveAccount(account.getUuid());
        Assertions.assertEquals(account.getStatus(), Account.Status.APPROVED);
    }

    @Test
    void cancelAccount() {
        am.createAccount("alice", "savings");
        Account account = p.getAccount("alice", "savings");
        am.cancelAccount(account.getUuid());
        Assertions.assertEquals(account.getStatus(), Account.Status.CANCELLED);
    }
}