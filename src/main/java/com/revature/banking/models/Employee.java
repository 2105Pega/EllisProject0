package com.revature.banking.models;

import com.revature.banking.models.User;

import java.io.Serializable;

public class Employee extends User implements Serializable {
    public Employee(String username, String passwordHash) {
        super(username, passwordHash);
    }
}
