package com.revature.banking.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class ClientTest {
    Client client;
    @BeforeEach
    void setUp() {
        client = new Client("alice", "***");
    }

}