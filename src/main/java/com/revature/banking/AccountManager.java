package com.revature.banking;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.UUID;

public class AccountManager {
    Persistence p;
    static Logger logger = LogManager.getLogger(UserManager.class);

    AccountManager(Persistence p) {
        this.p = p;
    }

    public Account createAccount(String username, String accountName) {
        Account account = new Account(username, accountName);
        p.addAccount(account);
        Client client = (Client)p.getUser(username);
        if (client == null) {
            return null;
        }
        client.addAccountUUID(account.getUuid());
        logger.debug("Created new account for " + username + " named " +
                accountName + " with UUID " + account.getUuid().toString());
        return account;
    }

    public void addUser(String username, UUID accountUuid) {
        Account account = p.getAccount(accountUuid);
        ArrayList<String> currentAccountHolders = account.getAccountHolders();
        Boolean alreadyExists = currentAccountHolders.contains(username);
        if (!alreadyExists) {
            account.addAccountHolder(username);
            Client c = (Client)p.getUser(username);
            c.addAccountUUID(accountUuid);
        }
    }

    public void approveAccount(UUID uuid) {
        p.getAccount(uuid).setStatus(Account.Status.APPROVED);
    }

    public void cancelAccount(UUID uuid) {
        p.getAccount(uuid).setStatus(Account.Status.CANCELLED);
    }

}
