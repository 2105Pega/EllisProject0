package com.revature.banking.services;

import com.revature.banking.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransactionManager {
    Persistence p;
    static Logger logger = LogManager.getLogger(TransactionManager.class);

    public TransactionManager(Persistence p) {
        this.p = p;
    }

    public Boolean withdraw(double amount, Integer id) {
        Account account = p.getAccount(id);

        if (amount < 0) {
            logger.trace("withdraw failed due to invalid amount");
            return false;
        } else if (amount > account.getBalance()) {
            logger.trace("withdraw failed due to insufficient funds");
            return false;
        } else if (!account.getStatus().equals(Account.Status.APPROVED)) {
            logger.trace("withdraw failed because account is not approved");
            return false;
        }else {
            account.setBalance(account.getBalance() - amount);
            p.addTransaction(new Transaction(amount, id, Transaction.Type.WITHDRAW));
            logger.trace(String.format("successful withdraw of " + Format.f(amount) +
                    " from " + id.toString()));
            return true;
        }
    }

    public Boolean deposit(double amount, Integer id) {
        Account account = p.getAccount(id);
        if (account == null) {
            logger.error("deposit failed because account does not exist");
            return false;
        } else if (!account.getStatus().equals(Account.Status.APPROVED)) {
            logger.trace("transfer failed because account is not approved");
            return false;
        } else if (amount < 0) {
            logger.trace("deposit failed due to invalid amount");
            return false;
        } else {
            account.setBalance(account.getBalance() + amount);
            p.addTransaction(new Transaction(amount, id, Transaction.Type.DEPOSIT));
            logger.trace(String.format("successful deposit of " + Format.f(amount) + " to "
                    + id.toString()));
            return true;
        }
    }

    public Boolean transfer(double amount, Integer from, Integer to, String username) {
        Account source = p.getAccount(from);
        Account destination = p.getAccount(to);

        if (source == null) {
            logger.trace("transfer failed because source account does not exist");
            return false;
        } else if (destination == null) {
            logger.trace("transfer failed because destination account does not exist");
            return false;
        } else if (!source.getStatus().equals(Account.Status.APPROVED)) {
            logger.trace("transfer failed because source account is not approved");
            return false;
        } else if (!destination.getStatus().equals(Account.Status.APPROVED)) {
            logger.trace("transfer failed because destination account is not approved");
            return false;
        } else if (source.getBalance() < amount) {
            logger.trace("transfer failed due to insufficient funds");
            return false;
        } else if (source.getId().equals(destination.getId())) {
            logger.trace("transfer failed because source and destination are same account");
            return false;
        } else {
            source.setBalance(source.getBalance() - amount);
            destination.setBalance(destination.getBalance() + amount);
            p.addTransaction(new Transaction(amount, from, to, p.getUser(username).getId(), Transaction.Type.TRANSFER));
            logger.trace(String.format("successful transfer of " + Format.f(amount) + " from "
                    + from.toString() + " to " + to.toString()));
            return true;
        }
    }
}
