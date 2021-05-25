package com.revature.banking.services;


import com.revature.banking.models.Account;
import com.revature.banking.models.Client;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.UUID;

public class AccountManager {
    Persistence p;
    static Logger logger = LogManager.getLogger(UserManager.class);

    public AccountManager(Persistence p) {
        this.p = p;
    }

    public Account createAccount(String username, String accountName) {
        Account account = new Account(username, accountName);
        Integer accountId = p.addAccount(account);
        Client client = p.getUser(username);
        if (client == null) {
            return null;
        }
        Account newAccount = new Account(accountId, account.getBalance(), account.getStatus().toString(), account.getName());
        p.addAccountToUser(newAccount.getId(), client.getId());
        logger.debug("Created new account for " + username + " named " +
                accountName + " with ID " + account.getId().toString());
        return newAccount;
    }

    public void addUser(String username, Integer accountId) {
        Client client = p.getUser(username);
        p.addAccountToUser(accountId, client.getId());
    }

    public void approveAccount(Integer id) {
        p.approveAccount(id);
    }

    public void cancelAccount(Integer id) {
        p.removeAccount(p.getAccount(id));
    }

}
