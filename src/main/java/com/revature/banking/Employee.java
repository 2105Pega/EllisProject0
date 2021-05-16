package com.revature.banking;

import java.io.Serializable;

public class Employee extends User implements Serializable {
    Employee(String username, String passwordHash) {
        super(username, passwordHash);
    }
}
