package com.revature.banking;

import com.revature.banking.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserInterface {
    enum AccountType {CLIENT, EMPLOYEE};
    Persistence p;
    Input i;
    String input;
    double doubleInput;
    UserManager um;
    AccountManager am;
    TransactionManager tm;

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
        run();
    }

    private void login() {
        System.out.print("Input username: ");
        getString();
        String username = input;
        if (!um.userExists(username)) {
            System.out.println("User does not exist.");
            run();
            return;
        }
        System.out.print("Input password: ");
        getString();
        String password = input;

        if (um.verifyPassword(username, password)) {
            User user = um.getUser(username);
            System.out.println("Successful login.");
            session(user);
        } else {
            System.out.println("Invalid password.");
        }
    }

    private void create() {
        System.out.println("Create client(C) or employee(E) account?");
        getString();
        AccountType accountType;
        if (input.equals("C")) {
            accountType = AccountType.CLIENT;
        } else if (input.equals("E")) {
            accountType = AccountType.EMPLOYEE;
        } else {
            System.out.println("Invalid input.");
            return;
        }
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

        switch (accountType) {
            case CLIENT:
                um.createClient(username, password);
                break;
            case EMPLOYEE:
                um.createEmployee(username, password);
                break;
        }
    }

    private void session(User user) {
        if (user.getClass().getName().equals("com.revature.banking.Client")) {
            clientSession((Client) user);
        } else {
            employeeSession((Employee) user);
        }
    }

    private void employeeSession(Employee employee) {
        while (!input.equals("E")) {
            System.out.println("Do you want to approve an account(A), cancel an account(C), withdraw(W), deposit(D), transfer (T), view info(V), or exit(E)?");
            getString();
            select:
            switch (input) {
                case "A":
                    approveAccount(employee);
                    break;
                case "C":
                    cancelAccount(employee);
                    break;
                case "W":
                    employeeWithdraw(employee);
                    break;
                case "D":
                    employeeDeposit(employee);
                    break;
                case "T":
                    employeeTransfer(employee);
                    break;
                case "V":
                    viewInfo(employee);
                    break;
                case "E":
                    break;
                default:
                    break select;
            }
        }

    }

    private void viewInfo(Employee employee) {
        viewClients();
        System.out.println("\nWhich user do you want to view info for?");
        getString();
        Client client = (Client)p.getUser(input);
        showAccounts(client);
        showTransactions(client);
    }

    private void viewClients() {
        System.out.println("Existing clients are");
        for (User user : p.getUsers()) {
            if (user.getClass().getName().equals("com.revature.banking.Client")) {
                System.out.println(user.getUsername());
            }
        }
    }

    private void employeeTransfer(Employee employee) {
        viewClients();
        System.out.print("Username of account holder to transfer from: ");
        getString();
        String username = input;
        Client client1 = (Client)p.getUser(username);
        System.out.println("Which account do you want to transfer from? Available accounts are:");
        showAccounts(client1);
        getString();
        Account account = p.getAccount(client1.getUsername(), input);
        if (account == null) {
            System.out.println("Account does not exist.");
            session(client1);
        } else {
            System.out.print("Username of account holder to transfer to: ");
            getString();
            String targetAccountUsername = input;
            System.out.print("Which account do you want to transfer to? Available accounts are: ");
            getString();
            String targetAccountName = input;
            System.out.print("Input amount: $");
            getDouble();
            Account targetAccount = p.getAccount(targetAccountUsername, targetAccountName);
            if (tm.transfer(doubleInput, account.getUuid(), targetAccount.getUuid())) {
                System.out.println("Transfer succeeded.");
            } else {
                System.out.println("Transfer failed.");
            }
        }
    }

    private void employeeDeposit(Employee employee) {
        viewClients();
        System.out.print("Username of account holder: ");
        getString();
        String username = input;
        Client client = (Client)p.getUser(username);
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
        UUID uuid = account.getUuid();
        if (tm.deposit(doubleInput, uuid)) {
            System.out.println("Deposit successful.");
        } else {
            System.out.println("Withdraw failed.");
        }
    }

    private void employeeWithdraw(Employee employee) {
        viewClients();
        System.out.print("Username of account holder: ");
        getString();
        String username = input;
        Client client = (Client)p.getUser(username);
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
        UUID uuid = account.getUuid();
        if (tm.withdraw(doubleInput, uuid)) {
            System.out.println("Withdraw successful.");
        } else {
            System.out.println("Withdraw failed.");
        }
    }

    private void approveAccount(Employee employee) {
        viewClients();
        System.out.print("Username of account holder: ");
        getString();
        String username = input;
        Client client = (Client)p.getUser(username);
        showAccounts(client);
        System.out.println("Name of account to approve: ");
        getString();
        String accountName = input;
        Account account = p.getAccount(username, accountName);
        if (account == null) {
            System.out.println("Account does not exist.");
            return;
        }
        UUID uuid = account.getUuid();
        am.approveAccount(uuid);
    }

    private void cancelAccount(Employee employee) {
        viewClients();
        System.out.print("Username of account holder: ");
        getString();
        String username = input;
        Client client = (Client)p.getUser(username);
        showAccounts(client);
        System.out.println("Name of account to cancel: ");
        getString();
        String accountName = input;
        Account account = p.getAccount(username, accountName);
        if (account == null) {
            System.out.println("Account does not exist.");
            return;
        }
        UUID uuid = account.getUuid();
        am.cancelAccount(uuid);
    }

    private void clientSession(Client client) {
        while(!input.equals("E")) {
            System.out.println("Do you want to apply for an account(A), withdraw(W), deposit(D), transfer(T), view accounts(V), or exit(E)?");
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
        Account newAccount = am.createAccount(client.getUsername(), accountName);

        if (joint) {
            ArrayList<String> existingAccountsJoint = um.getAccountNames(jointUsername);
            if (existingAccounts.contains(client.getUsername())) {
                System.out.println("Joint holder already has an account with that name, you cannot create accounts with duplicate names.");
                return;
            }
            am.addUser(jointUsername, newAccount.getUuid());
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
            if (tm.withdraw(doubleInput, account.getUuid())) {
                System.out.println("Withdraw successful.");
            } else {
                System.out.println("Withdraw failed.");
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
            if (tm.deposit(doubleInput, account.getUuid())) {
                System.out.println("Withdraw successful.");
            } else {
                System.out.println("Deposit failed.");
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
            session(user);
        } else {
            System.out.print("Username of account holder to transfer to: ");
            getString();
            String targetAccountUsername = input;
            System.out.print("Which account do you want to transfer to? Available accounts are: ");
            getString();
            String targetAccountName = input;
            System.out.print("Input amount: $");
            getDouble();
            Account targetAccount = p.getAccount(targetAccountUsername, targetAccountName);
            if (tm.transfer(doubleInput, account.getUuid(), targetAccount.getUuid())) {
                System.out.println("Transfer succeeded.");
            } else {
                System.out.println("Transfer failed.");
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

        UUID accounts[] = client.getAccountUUIDs();
        for (UUID accountId : accounts) {
            Account account = p.getAccount(accountId);
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
                System.out.println(account.getName() + " status is " + accountStatus + " balance is " + Format.f(account.getBalance()));
                System.out.print("Account holders are ");
                for (String accountHolder : account.getAccountHolders()) {
                    System.out.print(accountHolder + " ");
                }
                System.out.println();
            }
        }
    }

    private void showTransactions(Client client) {
        if (client == null) return;
        UUID accounts[] = client.getAccountUUIDs();
        List<Transaction> transactions = p.getTransactions();
        for (Transaction transaction : transactions) {
            for (UUID account : accounts) {
                if (transaction.getAccount().equals(account)) {
                    double amount = transaction.getAmount();
                    String accountName = p.getAccount(account).getName();
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
                        Transfer transfer = (Transfer)transaction;
                        String destination = p.getAccount(((Transfer) transaction).getDestination()).getName();
                        System.out.println(" to " + destination);
                    } else {
                        System.out.println("");
                    }
                }
            }
        }
    }
}
