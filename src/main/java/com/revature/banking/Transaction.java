package com.revature.banking;

import java.io.Serializable;
import java.util.UUID;

public abstract class Transaction implements Serializable {
    private final double amount;
    private final UUID account;
    private final int id;
    private Type type;
    enum Type {
        WITHDRAW,
        DEPOSIT,
        TRANSFER
    }

    public Transaction(double amount, UUID account, Type type) {
        this.amount = amount;
        this.account = account;
        this.type = type;
        this.id = this.hashCode();
    }

    public double getAmount() { return amount; }

    public UUID getAccount() {
        return account;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

}
