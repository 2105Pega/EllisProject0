package com.revature.banking.dao;

import com.revature.banking.models.Account;
import com.revature.banking.services.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountDao implements Dao<Account, Integer>, Serializable {
    private ArrayList<Account> accounts;
    static Logger logger = LogManager.getLogger(AccountDao.class);

    @Override
    public ArrayList<Account> getAll() {
        return accounts;
    }

    @Override
    public Account get(Integer id) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from accounts where account_id = ?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            result.next();

            return new Account(result.getInt("client_id"),
                    result.getDouble("balance"),
                    result.getString("account_status"),
                    result.getString("account_name"));
        } catch (SQLException e) {
            logger.error("error in database access when retrieving account by id");
            return null;
        }
    }

    @Override
    public void add(Account account) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "insert into accounts(balance, account_status, account_name) values(?,?,?)";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getStatus().toString());
            statement.setString(3, account.getName());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Account account) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "delete from accounts where " +
                    "balance = ? " +
                    "and account_status = ? " +
                    "and account_name = ? ";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getStatus().toString());
            statement.setString(3, account.getName());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Account get(Integer clientId, String accountName) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select get_account(?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, clientId);
            statement.setString(2, accountName);
            ResultSet result = statement.executeQuery();
            result.next();
            ArrayList<Account> accounts = new ArrayList<>();
            return new Account(result.getInt("employee_id"),
                    result.getDouble("balance"),
                    result.getString("account_status"),
                    result.getString("account_name"));

        } catch (SQLException e) {
            logger.error("error in database access when retrieving account by id");
            return null;
        }
    }
}
