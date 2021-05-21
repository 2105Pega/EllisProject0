package com.revature.banking.services;

import com.revature.banking.dao.*;
import com.revature.banking.models.Account;
import com.revature.banking.models.Client;
import com.revature.banking.models.Transaction;
import com.revature.banking.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Persistence {
    private UserDao users;
    private AccountDao accounts;
    private TransactionDao transactions;

    public Persistence() {
        users = new UserDao();
        accounts = new AccountDao();
        transactions = new TransactionDao();
    }

    //user management
    public ArrayList<Client> getUsers() {
        return users.getAll();
    }
    public Client getUser(Integer id) {
        return users.get(id);
    }

    public Client getUser(String username) {
        return users.get(username);
    }

    public void addUser(Client user) {
        users.add(user);
    }

    //account management
    public ArrayList<Account> getAccounts() {
        return accounts.getAll();
    }

    public ArrayList<Account> getAccounts(Integer clientId) {
        return accounts.getAll(clientId);
    }

    public Account getAccount(Integer id) {
        return accounts.get(id);
    }

    public Account getAccount(Integer id, String accountName) {
        return accounts.get(id, accountName);
    }

    public Account getAccount(String username, String accountName) {
        Integer clientId = getUser(username).getId();
        return accounts.get(clientId, accountName);
    }

    public Integer addAccount(Account account) {
        return accounts.add(account);
    }

    public void addAccountToUser(Integer accountId, Integer clientId) {
        users.addAccountToUser(accountId, clientId);
    }

    //transaction management
    public List<Transaction> getTransactions() {
        return transactions.getAll();
    }

    public Transaction getTransaction(int id) {
        return transactions.get(id);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
