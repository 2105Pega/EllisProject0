package com.revature.banking;

import java.io.Serializable;
import java.util.UUID;

public class Withdraw extends Transaction implements Serializable {
    Withdraw(double amount, UUID uuid) {
        super(amount, uuid, Type.WITHDRAW);
    }
}
