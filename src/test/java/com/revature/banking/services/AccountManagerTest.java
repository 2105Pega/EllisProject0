package com.revature.banking.services;

import com.revature.banking.models.Account;
import com.revature.banking.models.Client;
import com.revature.banking.services.AccountManager;
import com.revature.banking.services.Persistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class AccountManagerTest {
    AccountManager am;
    Persistence p;

    Account savings = new Account(1, 0, "APPROVED", "savings");
    Account checking = new Account(2, 0, "CANCELLED", "checking");
    Account bobSavings = new Account(3, 0, "PENDING", "savings");
    @BeforeEach
    void setup() {
        p = mock(Persistence.class);
        when(p.addAccount(savings)).thenReturn(1);
        when(p.addAccount(checking)).thenReturn(2);
        when(p.getUser("alice")).thenReturn(new Client("alice", "***", 1));
        when(p.getUser("bob")).thenReturn(new Client("bob", "***", 1));
        when(p.getAccount("alice", "savings")).thenReturn(savings);
        when(p.getAccount("alice", "checking")).thenReturn(checking);
        when(p.getAccount("bob", "savings")).thenReturn(bobSavings);
        when(p.getAccount(checking.getId())).thenReturn(checking);
        doNothing().when(p).removeAccount(isA(Account.class));
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
        am.addUser("bob", account.getId());
        Assertions.assertNotEquals(p.getAccount("bob", "savings"), null);
    }

    @Test
    void approveAccount() {
        am.createAccount("alice", "savings");
        Account account = p.getAccount("alice", "savings");
        am.approveAccount(account.getId());
        Assertions.assertEquals(account.getStatus(), Account.Status.APPROVED);
    }

    @Test
    void cancelAccount() {
        am.createAccount("alice", "checking");
        Account account = p.getAccount("alice", "checking");
        am.cancelAccount(account.getId());
        Assertions.assertEquals(account.getStatus(), Account.Status.CANCELLED);
    }
}