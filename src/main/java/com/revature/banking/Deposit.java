package com.revature.banking;

import java.util.UUID;

public class Deposit extends Transaction {
    Deposit(double amount, UUID uuid) {
        super(amount, uuid, Type.DEPOSIT);
    }
}
