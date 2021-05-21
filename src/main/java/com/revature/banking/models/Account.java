package com.revature.banking.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    public enum Status {
        PENDING,
        APPROVED,
        CANCELLED
    }

    private double balance;
    private ArrayList<String> accountHolders;
    private Status status;
    private final String name;
    private final Integer id;

    public Account(String username, String accountName) {
        balance = 0;
        accountHolders = new ArrayList<String>();
        accountHolders.add(username);
        id = 0;
        status = Status.PENDING;
        this.name = accountName;
    }

    public Account(
            Integer id,
            double balance,
            String status,
            String accountName) {
        this.id = id;
        this.balance = balance;
        this.name = accountName;
        this.status = Status.valueOf(status);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addAccountHolder(String client) {
        accountHolders.add(client);
    }

    public ArrayList<String> getAccountHolders() {
        return accountHolders;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
