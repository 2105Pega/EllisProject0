package com.revature.banking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class TransactionManager {
    Persistence p;
    static Logger logger = LogManager.getLogger(TransactionManager.class);

    TransactionManager(Persistence p) {
        this.p = p;
    }

    public Boolean withdraw(double amount, UUID uuid) {
        Account account = p.getAccount(uuid);
        ;
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
            p.addTransaction(new Withdraw(amount, uuid));
            logger.trace(String.format("successful withdraw of " + Format.f(amount) +
                    " from " + uuid.toString()));
            return true;
        }
    }

    public Boolean deposit(double amount, UUID uuid) {
        Account account = p.getAccount(uuid);
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
            p.addTransaction(new Deposit(amount, uuid));
            logger.trace(String.format("successful deposit of " + Format.f(amount) + " to "
                    + uuid.toString()));
            return true;
        }
    }

    public Boolean transfer(double amount, UUID from, UUID to) {
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
        } else if (source.getUuid().equals(destination.getUuid())) {
            logger.trace("transfer failed because source and destination are same account");
            return false;
        } else {
            source.setBalance(source.getBalance() - amount);
            destination.setBalance(destination.getBalance() + amount);
            p.addTransaction(new Transfer(amount, from, to));
            logger.trace(String.format("successful transfer of " + Format.f(amount) + " from "
                    + from.toString() + " to " + to.toString()));
            return true;
        }
    }
}
