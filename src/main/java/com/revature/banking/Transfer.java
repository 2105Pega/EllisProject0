package com.revature.banking;

import java.io.Serializable;
import java.util.UUID;

public class Transfer extends Transaction implements Serializable {
    final UUID destination;

    Transfer(double amount, UUID source, UUID destination) {
        super(amount, source, Type.TRANSFER);
        this.destination = destination;
    }

    public UUID getDestination() {
        return destination;
    }
}
