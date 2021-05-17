package com.revature.banking.models;

import java.io.Serializable;
import java.util.UUID;

public class Transfer extends Transaction implements Serializable {
    final UUID destination;

    public Transfer(double amount, UUID source, UUID destination) {
        super(amount, source, Type.TRANSFER);
        this.destination = destination;
    }

    public UUID getDestination() {
        return destination;
    }
}
