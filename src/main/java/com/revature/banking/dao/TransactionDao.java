package com.revature.banking.dao;

import com.revature.banking.models.Transaction;

import java.io.Serializable;
import java.util.ArrayList;

public class TransactionDao implements Dao<Transaction, Integer>, Serializable {
    private ArrayList<Transaction> transactions;

    public TransactionDao() {
        transactions = new ArrayList<Transaction>();
    }

    @Override
    public ArrayList<Transaction> getAll() {
        return transactions;
    }

    @Override
    public Transaction get(Integer id) {
        for (Transaction transaction: transactions) {
            if (transaction.getId() == id) {
                return transaction;
            }
        }
        return null;
    }

    @Override
    public void add(Transaction transaction) {
        transactions.add(transaction);
    }
}
