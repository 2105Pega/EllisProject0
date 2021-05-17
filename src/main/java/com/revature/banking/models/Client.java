package com.revature.banking.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Client extends User implements Serializable {
    private ArrayList<UUID> accountUUIDs;

    public Client(String username, String passwordHash) {
        super(username, passwordHash);
        accountUUIDs = new ArrayList<UUID>();
    }

    public void addAccountUUID(UUID uuid) {
        accountUUIDs.add(uuid);
    }

    public UUID[] getAccountUUIDs() {
        return accountUUIDs.toArray(new UUID[accountUUIDs.size()]);
    }
}
