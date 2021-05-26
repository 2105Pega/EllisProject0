package com.revature.banking;

import com.revature.banking.exceptions.TransactionFailedException;
import com.revature.banking.models.*;
import com.revature.banking.services.*;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserInterface {
    Persistence p;
    Input i;
    String input;
    double doubleInput;
    UserManager um;
    AccountManager am;
    TransactionManager tm;
    static Logger logger = LogManager.getLogger(UserInterface.class);

    public UserInterface(Persistence p, Input i) {
        this.p = p;
        this.i = i;
        um = new UserManager(p);
        am = new AccountManager(p);
        tm = new TransactionManager(p);
        input = "";
    }

    public void run() {
        input = "";
        while (!input.equals("E")) {
            System.out.println("Log in to existing profile(L), create new profile(C), or exit(E)?");
            getString();
            select:
            switch (input) {
                case "L":
                    login();
                    break;
                case "C":
                    create();
                    break;
                case "E":
                    return;
                default:
                    getString();
                    break select;
            }
        }
        if (input != "E") {
            run();
        }
    }

    private void login() {
        System.out.print("Input username: ");
        getString();
        String username = input;
        if (!um.userExists(username) && !PropertiesLoader.getAdminUsername().equals(username)) {
            System.out.println("User does not exist.");
            return;
        }
        System.out.print("Input password: ");
        getString();
        String password = input;

        if (PropertiesLoader.getAdminUsername().equals(username)) {
            if (PropertiesLoader.getAdminPassword().equals(password)) {
                employeeSession();
                return;
            } else {
                logger.info("failed login by " + username);
                System.out.println("Invalid password.");
                return;
            }
        }
        if (um.verifyPassword(username, password)) {
            User user = um.getUser(username);
            System.out.println("Successful login.");
            logger.info("successful login by " + username);
            session(user);
        } else {
            logger.info("failed login by " + username);
            System.out.println("Invalid password.");
        }
    }

    private void create() {
        System.out.print("Input username: ");
        getString();
        String username = input;
        if (um.userExists(username)) {
            System.out.println("User already exists.");
            return;
        }
        System.out.print("Input password: ");
        getString();
        String password = input;
        um.createClient(username, password);
    }

    private void session(User user) {
        clientSession((Client) user);
    }

    private void employeeSession() {
        while (!input.equals("E")) {
            System.out.println("Do you want to approve an account(A), cancel an account(C), remove client(R), withdraw(W), deposit(D), transfer (T), view info(V), view logs(L), or exit(E)?");
            getString();
            select:
            switch (input) {
                case "A":
                    approveAccount();
                    break;
                case "C":
                    cancelAccount();
                    break;
                case "R":
                    deleteClient();
                    break;
                case "W":
                    employeeWithdraw();
                    break;
                case "D":
                    employeeDeposit();
                    break;
                case "T":
                    employeeTransfer();
                    break;
                case "V":
                    viewInfo();
                    break;
                case "L":
                    viewLogs();
                    break;
                case "E":
                    break;
                default:
                    break select;
            }
        }

    }

    private void deleteClient() {
        viewClients();
        System.out.println("Which client do you want to remove?");
        getString();
        String username = input;
        if (um.userExists(username)) {
            um.deleteUser(username);
            System.out.println("User " + username + " removed.");
        } else {
            System.out.println("User " + username + "does not exist.");
        }
    }

    private void viewInfo() {
        viewClients();
        System.out.println("\nWhich client do you want to view info for?");
        getString();
        Client client = p.getUser(input);
        showAccounts(client);
        showTransactions(client);
    }

    private void viewClients() {
        System.out.println("Existing clients are: ");
        for (User user : p.getUsers()) {
            System.out.println(user.getUsername());
        }
    }

    private void employeeTransfer() {
        viewClients();
        System.out.print("Username of account holder to transfer from: ");
        getString();
        String username = input;
        if (!um.userExists(input)) {
            System.out.println("User does not exist.");
            return;
        }
        Client client1 = p.getUser(username);
        System.out.println("Which account do you want to transfer from? Available accounts are:");
        showAccounts(client1);
        getString();
        Account account = p.getAccount(client1.getUsername(), input);
        if (account == null) {
            System.out.println("Account does not exist.");
            return;
        } else {
            System.out.print("Username of account holder to transfer to: ");
            getString();
            if (!um.userExists(input)) {
                System.out.println("User does not exist.");
                return;
            }
            String targetAccountUsername = input;
            Client client2 = p.getUser(targetAccountUsername);
            System.out.println("Which account do you want to transfer to? Available accounts are: ");
            showAccounts(client2);
            getString();
            String targetAccountName = input;

            Account targetAccount = p.getAccount(targetAccountUsername, targetAccountName);
            if (targetAccount == null) {
                System.out.println("Account does not exist.");
                return;
            }
            try {
                System.out.print("Input amount: $");
                getDouble();
                tm.transfer(doubleInput, account.getId(), targetAccount.getId(), "admin");
                System.out.println("Transfer succeeded.");
            } catch (TransactionFailedException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private void employeeDeposit() {
        viewClients();
        System.out.print("Username of account holder: ");
        getString();
        String username = input;
        Client client = (Client) p.getUser(username);
        showAccounts(client);
        System.out.println("Name of account to deposit to: ");
        getString();
        String accountName = input;
        Account account = p.getAccount(username, accountName);
        if (account == null) {
            System.out.println("Account does not exist.");
            return;
        }
        System.out.print("Input amount: $");
        getDouble();
        Integer id = account.getId();
        try {
            tm.deposit(doubleInput, id);
            System.out.println("Deposit successful.");
        } catch (TransactionFailedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void employeeWithdraw() {
        viewClients();
        System.out.print("Username of account holder: ");
        getString();
        String username = input;
        Client client = (Client) p.getUser(username);
        showAccounts(client);
        System.out.println("Name of account to withdraw from: ");
        getString();
        String accountName = input;
        Account account = p.getAccount(username, accountName);
        if (account == null) {
            System.out.println("Account does not exist.");
            return;
        }
        System.out.print("Input amount: $");
        getDouble();
        Integer id = account.getId();
        try {
            tm.withdraw(doubleInput, id);
            System.out.println("Withdraw successful.");
        } catch (TransactionFailedException e) {
            System.out.println(e.getMessage());
        }

    }

    private void approveAccount() {
        viewClients();
        System.out.print("Username of account holder: ");
        getString();
        String username = input;
        Client client = (Client) p.getUser(username);
        showAccounts(client);
        System.out.print("Name of account to approve: ");
        getString();
        String accountName = input;
        Account account = p.getAccount(username, accountName);
        if (account == null) {
            System.out.println("Account does not exist.");
            return;
        }
        am.approveAccount(account.getId());
        System.out.println(username + "'s account " + accountName + " approved.");
    }

    private void cancelAccount() {
        viewClients();
        System.out.print("Username of account holder: ");
        getString();
        String username = input;
        Client client = (Client) p.getUser(username);
        showAccounts(client);
        System.out.println("Name of account to cancel: ");
        getString();
        String accountName = input;
        Account account = p.getAccount(username, accountName);
        if (account == null) {
            System.out.println("Account does not exist.");
            return;
        }
        Integer id = account.getId();
        Double balance = account.getBalance();
        if (balance.equals(0.0)) {
            am.cancelAccount(id);
            p.removeTransactionsWithNoAccount();
            System.out.println("Cancellation successful.");
        } else {
            System.out.println("You can only cancel accounts with a balance of $0.00.");
        }
    }

    private void cancelAccount(Client client) {
        System.out.println("Name of account to cancel: ");
        getString();
        String accountName = input;
        Account account = p.getAccount(client.getUsername(), accountName);
        if (account == null) {
            System.out.println("Account does not exist.");
            return;
        }
        Integer id = account.getId();
        Double balance = account.getBalance();
        if (balance.equals(0.0)) {
            am.cancelAccount(id);
            p.removeTransactionsWithNoAccount();
            System.out.println("Cancellation successful.");
        } else {
            System.out.println("You can only cancel accounts with a balance of $0.00.");
        }
    }

    private void clientSession(Client client) {
        while (!input.equals("E")) {
            System.out.println("Do you want to apply for an account(A), withdraw(W), deposit(D), transfer(T), view accounts(V), cancel account(C), or exit(E)?");
            getString();
            select:
            switch (input) {
                case "A":
                    apply(client);
                    break;
                case "W":
                    withdraw(client);
                    break;
                case "D":
                    deposit(client);
                    break;
                case "T":
                    transfer(client);
                    break;
                case "V":
                    showAccounts(client);
                    showTransactions(client);
                    break;
                case "C":
                    cancelAccount(client);
                    break;
                case "E":
                    break;
                default:
                    getString();
                    break select;
            }
        }
    }

    void apply(Client client) {
        String jointUsername = "";
        System.out.println("Do you want to make a joint account (Y/N)?");
        getString();
        Boolean joint = false;
        select:
        switch (input) {
            case "Y":
                joint = true;
                break;
            case "N":
                joint = false;
                break;
            default:
                System.out.println("Invalid input.");
                getString();
                break select;
        }
        if (joint) {
            System.out.print("Input username of the other account holder: ");
            getString();
            jointUsername = input;
            if (!um.userExists(jointUsername)) {
                System.out.println("User does not exist.");
                return;
            }
        }
        System.out.println("What name do you want your account to have?");
        getString();
        String accountName = input;
        ArrayList<String> existingAccounts = um.getAccountNames(client.getUsername());
        if (existingAccounts.contains(client.getUsername())) {
            System.out.println("Cannot create accounts with duplicate names.");
            return;
        }
        Account newAccount;

        if (joint) {
            ArrayList<String> existingAccountsJoint = um.getAccountNames(jointUsername);
            if (existingAccountsJoint.contains(jointUsername)) {
                System.out.println("Joint holder already has an account with that name, you cannot create accounts with duplicate names.");
                return;
            }
            newAccount = am.createAccount(client.getUsername(), accountName);
            am.addUser(jointUsername, newAccount.getId());
        } else {
            newAccount = am.createAccount(client.getUsername(), accountName);
        }
        System.out.println("Account created, it must be approved by an employee before use.");
    }

    void withdraw(Client user) {
        System.out.println("Which account do you want to withdraw from? Available accounts are:");
        showAccounts(user);
        getString();
        Account account = p.getAccount(user.getUsername(), input);
        if (account == null) {
            System.out.println("Account does not exist.");
            session(user);
        } else {
            System.out.print("Input amount: $");
            getDouble();
            try {
                tm.withdraw(doubleInput, account.getId());
                System.out.println("Withdraw successful.");
            } catch (TransactionFailedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void deposit(Client user) {
        System.out.println("Which account do you want to deposit to? Available accounts are:");
        showAccounts(user);
        getString();
        Account account = p.getAccount(user.getUsername(), input);
        if (account == null) {
            System.out.println("Account does not exist.");
            session(user);
        } else {
            System.out.print("Input amount: $");
            getDouble();
            try {
                tm.deposit(doubleInput, account.getId());
                System.out.println("Deposit successful.");
            } catch (TransactionFailedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void transfer(Client user) {
        System.out.println("Which account do you want to transfer from? Available accounts are:");
        showAccounts(user);
        getString();
        Account account = p.getAccount(user.getUsername(), input);
        if (account == null) {
            System.out.println("Account does not exist.");
        } else {
            System.out.println("Which account do you want to transfer to?");
            getString();
            String targetAccountName = input;

            Account targetAccount = p.getAccount(user.getUsername(), targetAccountName);
            if (targetAccount == null) {
                System.out.println("Account does not exist.");
            } else {
                try {
                    System.out.print("Input amount: $");
                    getDouble();
                    tm.transfer(doubleInput, account.getId(), targetAccount.getId(), user.getUsername());
                    System.out.println("Transfer succeeded.");
                } catch (TransactionFailedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void getString() {
        input = i.getLine();
    }

    private void getDouble() {
        doubleInput = i.getDouble();
        i.getLine();
    }

    private void showAccounts(Client client) {
        if (client == null) return;
        ArrayList<Account> accounts = p.getAccounts(client.getId());
        for (Account account : accounts) {
            String accountStatus;
            switch (account.getStatus()) {
                case APPROVED:
                    accountStatus = "approved";
                    break;
                case PENDING:
                    accountStatus = "pending";
                    break;
                case CANCELLED:
                    accountStatus = "canceled";
                    break;
                default:
                    accountStatus = "";
            }
            if (!accountStatus.equals("canceled")) {
                System.out.print(account.getName() + " status is " + accountStatus + " balance is " + Format.f(account.getBalance()));
                System.out.print(" account holders are ");
                for (Client accountHolder : p.getAllAccountHolders(account.getId())) {
                    System.out.print(accountHolder.getUsername() + " ");
                }
                System.out.println();
            }
        }
    }

    private void showTransactions(Client client) {
        if (client == null) return;
        HashSet<Transaction> transactions = new HashSet<>();
        ArrayList<Transaction> transactionArray = new ArrayList<>();

        for (Account account : p.getAccounts(client.getId())) {
            for (Transaction transaction : p.getTransactionsFromAccount(account.getId())) {
                if (!transactionArray.contains(transaction)) {
                    transactionArray.add(transaction);
                }
            }
        }
        Collections.sort(transactionArray);

        for (Transaction transaction : transactionArray) {
            double amount = transaction.getAmount();
            Account account = p.getAccount(transaction.getAccount());
            String accountName;
            if (account == null) {
                 accountName = "deleted account";
            } else {
                 accountName = p.getAccount(transaction.getAccount()).getName();
            }
            switch (transaction.getType()) {
                case WITHDRAW:
                    System.out.print("Withdraw from ");
                    break;
                case DEPOSIT:
                    System.out.print("Deposit to ");
                    break;
                case TRANSFER:
                    System.out.print("Transfer from ");
                    break;
                default:
                    break;
            }
            System.out.print(accountName + " of " + Format.f(amount));
            if (transaction.getType().equals(Transaction.Type.TRANSFER)) {
                String destination = p.getAccount(transaction.getDestination()).getName();
                Boolean initiatorExist = !transaction.getInitiator().equals(0);
                if (initiatorExist) {
                    Client initiator = p.getUser(transaction.getInitiator());
                    if (initiator == null) {
                        System.out.println(" to " + destination + " by deleted user");
                    } else {
                        System.out.println(" to " + destination + " by " + p.getUser(transaction.getInitiator()).getUsername());
                    }
                } else {
                    System.out.println(" to " + destination + " by admin");
                }
            } else {
                System.out.println("");
            }
        }
    }

    private void viewLogs() {
        System.out.println("Displaying logs:\n");
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from logs";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                System.out.println(result.getString("eventdate") + " " +
                        result.getString("level") + " " +
                        result.getString("logger") + " " +
                        result.getString("message"));
            }

        } catch (SQLException e) {
            logger.error("error in database access when viewing logs");
        }
        System.out.println("");
    }
}
