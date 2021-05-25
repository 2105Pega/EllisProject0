package com.revature.banking.services;

import com.revature.banking.dao.*;
import com.revature.banking.models.Account;
import com.revature.banking.models.Client;
import com.revature.banking.models.Transaction;
import com.revature.banking.models.User;

import java.io.*;
import java.lang.reflect.Array;
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

    public List<Client> getAllAccountHolders(Integer accountId) {
        return users.getAllAccountHolders(accountId);
    }

    public void deleteUser(String username) {
        users.remove(getUser(username));
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
        Client client = getUser(username);
        if (client != null) {
            Integer clientId = client.getId();
            return accounts.get(clientId, accountName);
        }
        return null;
    }

    public Integer addAccount(Account account) {
        return accounts.add(account);
    }

    public void addAccountToUser(Integer accountId, Integer clientId) {
        users.addAccountToUser(accountId, clientId);
    }

    public void approveAccount(Integer accountId) {
        accounts.approveAccount(accountId);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    //transaction management
    public List<Transaction> getTransactions() {
        return transactions.getAll();
    }

    public Transaction getTransaction(int id) {
        return transactions.get(id);
    }

    public List<Transaction> getTransactionsFromAccount(Integer accountId) {
        return transactions.getTransactionsFromAccount(accountId);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeAccountAssociations(Integer clientId) {
        users.removeAccountAssociations(clientId);
    }

    public void setBalance(Account account, double balance) {
        accounts.setBalance(account, balance);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public void removeTransactionsWithNoAccount() {
        ArrayList<Transaction> transactionList = transactions.getAll();
        ArrayList<Account> accountList = getAccounts();
        for (Transaction transaction : transactionList) {
            Integer account1 = transaction.getAccount();
            Integer account2 = transaction.getDestination();
            if (!accountList.contains(getAccount(account1)) && !accountList.contains(getAccount(account2))) {
                removeTransaction(transaction);
            }
        }
    }
}
