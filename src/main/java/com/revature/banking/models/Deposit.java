package com.revature.banking.models;

import java.util.UUID;

public class Deposit extends Transaction {
    public Deposit(double amount, UUID uuid) {
        super(amount, uuid, Type.DEPOSIT);
    }
}
