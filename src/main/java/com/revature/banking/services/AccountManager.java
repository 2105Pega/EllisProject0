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
        p.addAccount(account);
        Client client = (Client)p.getUser(username);
        if (client == null) {
            return null;
        }
        client.addAccountID(account.getId());
        logger.debug("Created new account for " + username + " named " +
                accountName + " with UUID " + account.getId().toString());
        return account;
    }

    public void addUser(String username, Integer accountId) {
        Account account = p.getAccount(accountId);
        ArrayList<String> currentAccountHolders = account.getAccountHolders();
        Boolean alreadyExists = currentAccountHolders.contains(username);
        if (!alreadyExists) {
            account.addAccountHolder(username);
            Client c = (Client)p.getUser(username);
            c.addAccountID(accountId);
        }
    }

    public void approveAccount(Integer id) {
        p.getAccount(id).setStatus(Account.Status.APPROVED);
    }

    public void cancelAccount(Integer id) {
        p.getAccount(id).setStatus(Account.Status.CANCELLED);
    }

}
