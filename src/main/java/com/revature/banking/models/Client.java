package com.revature.banking.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client extends User implements Serializable {
    private Integer id;

    public Client(String username, String passwordHash, int id) {
        super(username, passwordHash);
        this.id = id;
    }

    public Client(String username, String passwordHash) {
        super(username, passwordHash);
        this.id = 0;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public List<Integer> getAccountIDs() {
        return new ArrayList<Integer>();
    }
}
