package com.revature.banking.dao;

import com.revature.banking.models.Transaction;
import com.revature.banking.services.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionDao implements Dao<Transaction, Integer>, Serializable {
    private ArrayList<Transaction> transactions;
    static Logger logger = LogManager.getLogger(TransactionDao.class);

    public TransactionDao() {
        transactions = new ArrayList<Transaction>();
    }

    @Override
    public ArrayList<Transaction> getAll() {
        return transactions;
    }

    @Override
    public Transaction get(Integer id) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from transactions where transaction_id = ?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            result.next();

            return new Transaction(result.getDouble("amount"),
                    result.getInt("source_id"),
                    result.getInt("destination_id"),
                    result.getString("transaction_type"),
                    result.getInt("transaction_id")) {
            };
        } catch (SQLException e) {
            logger.error("error in database access when retrieving transaction by id");
            return null;
        }
    }

    @Override
    public void add(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public void remove(Transaction itemToRemove) {

    }
}
