package com.revature.banking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    Client client;
    @BeforeEach
    void setUp() {
        client = new Client("alice", "***");
    }

    @Test
    void addAccountUUID() {
        UUID uuid = UUID.randomUUID();
        client.addAccountUUID(uuid);
        UUID []accountUUIDs = client.getAccountUUIDs();
        for (UUID accountUuid :accountUUIDs) {
            Assertions.assertEquals(accountUuid, uuid);
        }
    }

    @Test
    void getAccountUUIDs() {
        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        client.addAccountUUID(uuid);
        client.addAccountUUID(uuid2);
        Assertions.assertEquals(client.getAccountUUIDs().length, 2);
    }
}