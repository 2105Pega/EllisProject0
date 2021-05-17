package com.revature.banking.dao;

import com.revature.banking.Account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class AccountDao implements Dao<Account, UUID>, Serializable {
    private ArrayList<Account> accounts;

    public AccountDao() {
        accounts = new ArrayList<Account>();
    }

    @Override
    public ArrayList<Account> getAll() {
        return accounts;
    }

    @Override
    public Account get(UUID id) {
        for (Account account : accounts) {
            if (account.getUuid().equals(id)) {
                return account;
            }
        }
        return null;
    }

    @Override
    public void add(Account account) {
        accounts.add(account);
    }

    public Account get(String username, String accountName) {
        for (Account account : accounts) {
            ArrayList<String> accountHolders = account.getAccountHolders();
            if (accountHolders.contains(username)) {
                if (account.getName().equals(accountName)) {
                    return account;
                }
            }
        }
        return null;
    }
}
