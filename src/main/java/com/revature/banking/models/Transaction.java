package com.revature.banking.models;

import java.io.Serializable;

public class Transaction implements Serializable, Comparable<Transaction> {
    private final double amount;
    private final Integer account;
    private final Integer destination;
    private final Integer id;
    private final Integer initiator;
    private Type type;

    public enum Type {
        WITHDRAW,
        DEPOSIT,
        TRANSFER
    }

    //constructors for transfer transaction, includes destination field
    public Transaction(double amount, Integer account, Integer destination, Integer initiator, Type type) {
        this.amount = amount;
        this.account = account;
        this.destination = destination;
        this.initiator = initiator;
        this.type = type;
        this.id = 0;
    }
    public Transaction(double amount, Integer account, Integer destination, Integer initiator, String type) {
        this.amount = amount;
        this.account = account;
        this.destination = destination;
        this.initiator = initiator;
        this.type = Type.valueOf(type);
        this.id = 0;
    }

    public Transaction(double amount, Integer account, Integer destination, Integer initiator, String type, Integer id) {
        this.amount = amount;
        this.account = account;
        this.destination = destination;
        this.initiator = initiator;
        this.type = Type.valueOf(type);
        this.id = id;
    }

    //constructors for non-transfer transaction, does not include destination field
    public Transaction(double amount, Integer account, Type type) {
        this.amount = amount;
        this.account = account;
        this.destination = 0;
        this.type = type;
        this.id = 0;
        this.initiator = 0;
    }
    public Transaction(double amount, Integer account, String type) {
        this.amount = amount;
        this.account = account;
        this.destination = 0;
        this.type = Type.valueOf(type);
        this.id = 0;
        this.initiator = 0;
    }

    public Transaction(double amount, Integer account, String type, Integer id) {
        this.amount = amount;
        this.account = account;
        this.destination = 0;
        this.type = Type.valueOf(type);
        this.id = id;
        this.initiator = 0;
    }



    public double getAmount() { return amount; }

    public int getAccount() {
        return account;
    }

    public int getDestination() {
        return destination;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Integer getInitiator() {
        return initiator;
    }

    @Override
    public int compareTo(Transaction t) {
        return this.getId() - t.getId();
    }
}
