package com.revature.banking.services;

import com.revature.banking.models.Account;
import com.revature.banking.models.Client;
import com.revature.banking.models.Transaction;
import com.revature.banking.models.User;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;

public class UserManager {
    Persistence p;
    static Logger logger = LogManager.getLogger(UserManager.class);
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public UserManager(Persistence p) {
        this.p = p;
    }

    public Client createClient(String username, String password) {
        Client client = new Client(username, hashPassword(password));
        p.addUser(client);
        logger.trace("Created new client " + client.getUsername());
        return client;
    }

    public User getUser(String username) {
        return p.getUser(username);
    }

    public Boolean verifyPassword(String username, String password) {
        String passwordHash = hashPassword(password);
        return (p.getUser(username).getPasswordHash().equals(passwordHash));
    }

    public Boolean userExists(String username) {
        return (p.getUser(username) != null);
    }

    public Boolean clientExists(String username) {
        return (p.getUser(username) != null);
    }

    private String hashPassword(String password) {
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

    private String bytesToHex(byte[] bytes) {
        StringBuilder s = new StringBuilder("");
        for (byte b : bytes) {
            byte upperNibble = (byte)(b & 0x0F);
            byte lowerNibble = (byte)((b & 0xF0) >> 4);

            s.append(nibbleToChar(lowerNibble));
            s.append(nibbleToChar(upperNibble));
        }
        return s.toString();
    }

    private Character nibbleToChar(byte b) {
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
            List<Client> accountHolders = p.getAllAccountHolders(account.getId());
            if  (accountHolders == null) return accountNames;
            for (Client accountHolder : accountHolders) {
                if (accountHolder.getUsername().equals(username)) {
                    accountNames.add(account.getName());
                }
            }
        }
        return accountNames;
    }

    public ArrayList<Account> getAccountIDs(String username) {
        ArrayList<Account> accountIDs = new ArrayList<>();
        User user = getUser(username);
        ArrayList<Account> accounts = p.getAccounts();
        for (Account account : accounts) {
            List<Client> accountHolders = p.getAllAccountHolders(account.getId());
            if  (accountHolders == null) return accountIDs;
            for (Client accountHolder : accountHolders) {
                if (accountHolder.getUsername().equals(username)) {
                    accountIDs.add(account);
                }
            }
        }
        return accountIDs;
    }

    public void removeAssociatedTransactions(Integer id) {
        ArrayList<Account> accounts = p.getAccounts(id);
        //only remove the transaction if this client id is the only associated account holder
        for (Account account : accounts) {
            if (p.getAllAccountHolders(account.getId()).size() == 1) {
                for (Transaction transaction : p.getTransactionsFromAccount(account.getId())) {
                    p.removeTransaction(transaction);
                }
            }
        }
    }

    public void deleteUser(String username) {
        Integer id = p.getUser(username).getId();
        ArrayList<Account> accounts = getAccountIDs(username);
        ArrayList<Account> accountsToRemove = new ArrayList<>();
        for (Account account : accounts) {
            if (p.getAllAccountHolders(account.getId()).size() == 1) {
                accountsToRemove.add(account);
            }
        }
        p.removeAccountAssociations(id);
        p.deleteUser(username);
        for (Account account : accountsToRemove) {
            p.removeAccount(account);
        }
        p.removeTransactionsWithNoAccount();
    }
}
