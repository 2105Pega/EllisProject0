package com.revature.banking;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Account implements Serializable {
    enum Status {
        PENDING,
        APPROVED,
        CANCELLED
    }

    private double balance;
    private ArrayList<String> accountHolders;
    private final UUID uuid;
    private Status status;
    private final String name;

    public Account(String username, String accountName) {
        balance = 0;
        accountHolders = new ArrayList<String>();
        accountHolders.add(username);
        uuid = UUID.randomUUID();
        status = Status.PENDING;
        this.name = accountName;
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

    public UUID getUuid() {
        return uuid;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
