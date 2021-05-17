package com.revature.banking.services;

import com.revature.banking.models.Account;
import com.revature.banking.services.AccountManager;
import com.revature.banking.services.Persistence;
import com.revature.banking.services.TransactionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionManagerTest {
    Persistence p;
    TransactionManager tm;
    AccountManager am;
    Account account1, account2;
    @BeforeEach
    void setup() {
        p = new Persistence();
        tm = new TransactionManager(p);
        am = new AccountManager(p);
        account1 = new Account("alice", "checking");
        account2 = new Account("bob", "savings");
        p.addAccount(account1);
        p.addAccount(account2);
    }
    @Test
    void withdraw() {
        Assertions.assertEquals(tm.withdraw(500, account1.getUuid()), false);
        am.approveAccount(account1.getUuid());
        tm.deposit(500, account1.getUuid());
        Assertions.assertEquals(tm.withdraw(500, account1.getUuid()), true);
    }

    @Test
    void deposit() {
        Assertions.assertEquals(tm.deposit(500, account1.getUuid()), false);
        am.approveAccount(account1.getUuid());
        Assertions.assertEquals(tm.deposit(500, account1.getUuid()), true);
    }

    @Test
    void transfer() {
        Assertions.assertEquals(tm.transfer(500, account1.getUuid(), account2.getUuid()), false);
        am.approveAccount(account1.getUuid());
        am.approveAccount(account2.getUuid());
        tm.deposit(500, account1.getUuid());
        Assertions.assertEquals(tm.transfer(500, account1.getUuid(), account2.getUuid()), true);
    }
}
