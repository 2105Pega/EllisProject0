package com.revature.banking.services;

import com.revature.banking.dao.TransactionDao;
import com.revature.banking.exceptions.TransactionFailedException;
import com.revature.banking.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransactionManager {
    Persistence p;
    static Logger logger = LogManager.getLogger(TransactionManager.class);

    public TransactionManager(Persistence p) {
        this.p = p;
    }

    public Boolean withdraw(double amount, Integer id) throws TransactionFailedException {
        Account account = p.getAccount(id);

        if (amount < 0) {
            throw new TransactionFailedException("withdraw failed due to invalid amount");
        } else if (amount > account.getBalance()) {
            throw new TransactionFailedException("withdraw failed due to insufficient funds");
        } else if (!account.getStatus().equals(Account.Status.APPROVED)) {
            throw new TransactionFailedException("withdraw failed because account is not approved");
        } else {
            p.setBalance(account, account.getBalance() - amount);
            p.addTransaction(new Transaction(amount, id, Transaction.Type.WITHDRAW));
            logger.trace(String.format("successful withdraw of " + Format.f(amount) +
                    " from " + id.toString()));
            return true;
        }
    }

    public Boolean deposit(double amount, Integer id) throws TransactionFailedException {
        Account account = p.getAccount(id);
        if (account == null) {
            throw new TransactionFailedException("deposit failed because account does not exist");
        } else if (!account.getStatus().equals(Account.Status.APPROVED)) {
            throw new TransactionFailedException("transfer failed because account is not approved");
        } else if (amount < 0) {
            logger.trace("deposit failed due to invalid amount");
        } else {
            p.setBalance(account, account.getBalance() + amount);
            p.addTransaction(new Transaction(amount, id, Transaction.Type.DEPOSIT));
            logger.trace(String.format("successful deposit of " + Format.f(amount) + " to "
                    + id.toString()));
            return true;
        }
        return false;
    }

    public Boolean transfer(double amount, Integer from, Integer to, String username) throws TransactionFailedException {
        Account source = p.getAccount(from);
        Account destination = p.getAccount(to);

        if (source == null) {
            throw new TransactionFailedException("transfer failed because source account does not exist");
        } else if (destination == null) {
            throw new TransactionFailedException("transfer failed because destination account does not exist");
        } else if (!source.getStatus().equals(Account.Status.APPROVED)) {
            throw new TransactionFailedException("transfer failed because source account is not approved");
        } else if (!destination.getStatus().equals(Account.Status.APPROVED)) {
            throw new TransactionFailedException("transfer failed because destination account is not approved");
        } else if (source.getBalance() < amount) {
            throw new TransactionFailedException("transfer failed due to insufficient funds");
        } else if (source.getId().equals(destination.getId())) {
            throw new TransactionFailedException("transfer failed because source and destination are same account");
        } else {
            p.setBalance(source, source.getBalance() - amount);
            p.setBalance(destination, destination.getBalance() + amount);
            try {
                p.addTransaction(new Transaction(amount, from, to, p.getUser(username).getId(), Transaction.Type.TRANSFER));
            } catch (Exception e) {
                p.addTransaction(new Transaction(amount, from, to, 0, Transaction.Type.TRANSFER));
            }
            logger.trace(String.format("successful transfer of " + Format.f(amount) + " from "
                    + from.toString() + " to " + to.toString()));
            return true;
        }
    }

    public void remove(Transaction transaction) {
        p.removeTransaction(transaction);
    }
}
