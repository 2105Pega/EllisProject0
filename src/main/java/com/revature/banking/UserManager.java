package com.revature.banking;

import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

public class UserManager {
    Persistence p;
    static Logger logger = LogManager.getLogger(UserManager.class);

    UserManager(Persistence p) {
        this.p = p;
    }

    public Client createClient(String username, String password) {
        Client client = new Client(username, hashPassword(password));
        p.addUser(client);
        logger.trace("Created new client " + client.getUsername());
        return client;
    }

    public Employee createEmployee(String username, String password) {
        Employee employee = new Employee(username, hashPassword(password));
        p.addUser(employee);
        LogManager.getLogger("Created new employee + " + employee.getUsername());
        return employee;
    }

    public User getUser(String username) {
        return p.getUser(username);
    }

    public Boolean verifyPassword(String username, String password) {
        String passwordHash = hashPassword(password);
        return (p.getUser(username).getPasswordHash().equals(passwordHash));
    }

    public Boolean userExists(String username) {
        ArrayList<User> users = p.getUsers();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public Boolean clientExists(String username) {
        ArrayList<User> users = p.getUsers();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getClass().getName().equals("com.revature.banking.Client")) {
                    return true;
                }
            }
        }
        return false;
    }

    private String hashPassword(String password) {
        String hash = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] digest = messageDigest.digest();
            hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            System.out.println("password hashing failed");
        }
        return hash;
    }

    public ArrayList<String> getAccountNames(String username) {
        ArrayList<String> accountNames = new ArrayList<String>();
        User user = getUser(username);
        ArrayList<Account> accounts = p.getAccounts();
        for (Account account : accounts) {
            ArrayList<String> accountHolders = account.getAccountHolders();
            for (String accountHolder : accountHolders) {
                if (accountHolder.equals(username)) {
                    accountNames.add(account.getName());
                }
            }
        }
        return accountNames;
    }
}
