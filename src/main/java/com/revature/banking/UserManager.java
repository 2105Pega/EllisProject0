package com.revature.banking;

import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

public class UserManager {
    Persistence p;
    static Logger logger = LogManager.getLogger(UserManager.class);
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

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

    public String hashPassword(String password) {
        String hash = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] digest = messageDigest.digest();
            hash = bytesToHex(digest);
        } catch (Exception e) {
            System.out.println("password hashing failed");
        }
        return hash;
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder s = new StringBuilder("");
        for (byte b : bytes) {
            byte upperNibble = (byte)(b & 0x0F);
            byte lowerNibble = (byte)((b & 0xF0) >> 4);

            s.append(nibbleToChar(lowerNibble));
            s.append(nibbleToChar(upperNibble));
        }
        return s.toString();
    }

    public Character nibbleToChar(byte b) {
        switch (b) {
            case 0x0:
                return '0';
            case 0x1:
                return '1';
            case 0x2:
                return '2';
            case 0x3:
                return '3';
            case 0x4:
                return '4';
            case 0x5:
                return '5';
            case 0x6:
                return '6';
            case 0x7:
                return '7';
            case 0x8:
                return '8';
            case 0x9:
                return '9';
            case 0xA:
                return 'A';
            case 0xB:
                return 'B';
            case 0xC:
                return 'C';
            case 0xD:
                return 'D';
            case 0xE:
                return 'E';
            case 0xF:
                return 'F';
        }
        return 'X';
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
