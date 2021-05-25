package com.revature.banking.services;

import com.revature.banking.exceptions.TransactionFailedException;
import com.revature.banking.models.Account;
import com.revature.banking.services.AccountManager;
import com.revature.banking.services.Persistence;
import com.revature.banking.services.TransactionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionManagerTest {
    Persistence p;
    TransactionManager tm;
    AccountManager am;
    Account account1, account2;

    @BeforeEach
    void setup() {
        p = mock(Persistence.class);
        tm = new TransactionManager(p);
        am = new AccountManager(p);
        account1 = new Account(1, 0, "APPROVED", "checking");
        account2 = new Account(2, 1000, "APPROVED", "savings");

        when(p.getAccount(account1.getId())).thenReturn(account1);
        when(p.getAccount(account2.getId())).thenReturn(account1);
    }
    @Test
    void withdraw() {
        try {
            Assertions.assertThrows(TransactionFailedException.class, () -> {tm.withdraw(500, account1.getId());});
            Assertions.assertEquals(tm.withdraw(500, account2.getId()), true);
        } catch (Exception e) {

        }
    }

    @Test
    void deposit() {
        try {
            Assertions.assertThrows(TransactionFailedException.class, () -> {tm.deposit(500, 0);});
            Assertions.assertEquals(tm.deposit(500, account1.getId()), true);
        } catch (Exception e) {

        }
    }

    @Test
    void transfer() {
        try {
            Assertions.assertEquals(tm.transfer(500, account1.getId(), account2.getId(), "alice"), false);
            am.approveAccount(account1.getId());
            am.approveAccount(account2.getId());
            tm.deposit(500, account1.getId());
            Assertions.assertEquals(tm.transfer(500, account1.getId(), account2.getId(), "alice"), true);
        } catch (Exception e) {

        }
    }
}
