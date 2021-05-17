package com.revature.banking.models;

import java.io.Serializable;
import java.util.UUID;

public class Withdraw extends Transaction implements Serializable {
    public Withdraw(double amount, UUID uuid) {
        super(amount, uuid, Type.WITHDRAW);
    }
}
