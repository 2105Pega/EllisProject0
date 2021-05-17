package com.revature.banking;

import com.revature.banking.dao.*;
import com.revature.banking.models.Account;
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

    public Persistence(String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (UserDao) ois.readObject();
            accounts = (AccountDao) ois.readObject();
            transactions = (TransactionDao) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.writeObject(accounts);
            oos.writeObject(transactions);
            oos.close();
            fos.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    //user management
    public ArrayList<User> getUsers() {
        return users.getAll();
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void addUser(User user) {
        users.add(user);
    }

    //account management
    public ArrayList<Account> getAccounts() {
        return accounts.getAll();
    }

    public Account getAccount(UUID uuid) {
        return accounts.get(uuid);
    }

    public Account getAccount(String username, String accountName) {
        return accounts.get(username, accountName);
    }

    public void addAccount(Account account) {
        accounts.add(account);
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
