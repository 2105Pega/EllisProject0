package com.revature.banking.models;

import java.io.Serializable;
import java.util.UUID;

public class Transfer extends Transaction implements Serializable {
    final UUID destination;
    final String username;

    public Transfer(double amount, UUID source, UUID destination, String username) {
        super(amount, source, Type.TRANSFER);
        this.destination = destination;
        this.username = username;
    }

    public UUID getDestination() {
        return destination;
    }
    public String getUsername() {
        return username;
    }
}
