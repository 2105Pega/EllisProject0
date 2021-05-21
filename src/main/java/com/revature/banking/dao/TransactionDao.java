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
    static Logger logger = LogManager.getLogger(TransactionDao.class);

    @Override
    public ArrayList<Transaction> getAll() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from transactions";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                transactions.add(new Transaction(result.getDouble("amount"),
                        result.getInt("source_id"),
                        result.getInt("destination_id"),
                        result.getInt("initiator"),
                        result.getString("transaction_type")));
            }
            return transactions;
        } catch (SQLException e) {
            logger.error("error in database access when retrieving transactions");
            return null;
        }
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
                    result.getInt("initiator"),
                    result.getString("transaction_type"),
                    result.getInt("transaction_id")) {
            };
        } catch (SQLException e) {
            logger.error("error in database access when retrieving transaction by id");
            return null;
        }
    }

    @Override
    public Integer add(Transaction transaction) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "insert into transactions(source_id, destination_id, initiator, amount, transaction_type) values (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, transaction.getAccount());
            statement.setInt(2, transaction.getDestination());
            statement.setInt(3, transaction.getAccount());
            statement.setDouble(4, transaction.getAmount());
            statement.setString(5, transaction.getType().toString());

            statement.execute();
        } catch (SQLException e) {
            logger.error("error in database access when adding transaction");
        }
        return 0;
    }

    @Override
    public void remove(Transaction itemToRemove) {

    }
}
