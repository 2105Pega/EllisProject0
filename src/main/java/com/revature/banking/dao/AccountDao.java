package com.revature.banking.dao;

import com.revature.banking.models.Account;
import com.revature.banking.services.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

public class AccountDao implements Dao<Account, Integer>, Serializable {
    static Logger logger = LogManager.getLogger(AccountDao.class);

    /*
    --  Account Table Fields --

    account_id serial primary key,
    balance double precision,
    account_status varchar(10),
    account_name varchar(40)

     */

    @Override
    public ArrayList<Account> getAll() {
        ArrayList<Account> accounts = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from accounts";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                accounts.add(new Account(result.getInt("account_id"),
                        result.getDouble("balance"),
                        result.getString("account_status"),
                        result.getString("account_name")));
            }
            return accounts;
        } catch (SQLException e) {
            logger.error("error in database access when retrieving transactions");
            return null;
        }
    }

    public ArrayList<Account> getAll(Integer clientId) {
        ArrayList<Account> accounts = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select accounts.account_id, accounts.balance, accounts.account_status, accounts.account_name " +
                    "from ((clients_accounts " +
                    "inner join clients on clients.client_id = clients_accounts.client_id) " +
                    "inner join accounts on accounts.account_id = clients_accounts.account_id) " +
                    "where clients_accounts.client_id = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, clientId);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                accounts.add(new Account(result.getInt("account_id"),
                        result.getDouble("balance"),
                        result.getString("account_status"),
                        result.getString("account_name")));
            }
            return accounts;
        } catch (SQLException e) {
            logger.error("error in database access when retrieving accounts for client");
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public Account get(Integer id) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from accounts where account_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Account(result.getInt("account_id"),
                        result.getDouble("balance"),
                        result.getString("account_status"),
                        result.getString("account_name"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("error in database access when retrieving account by id");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer add(Account account) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "insert into accounts(balance, account_status, account_name) values(?,?,?)";

            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getStatus().toString());
            statement.setString(3, account.getName());

            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            result.next();
            return result.getInt(1);

        } catch (SQLException e) {
            logger.error("error in database access when adding account");
            e.printStackTrace();
            return 0;
        }
    }
    public void remove(Account account) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "delete from clients_accounts where account_id = ?;"
                    + "delete from accounts where account_id = ?";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, account.getId());
            statement.setInt(2, account.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("error in database access when removing account");
            e.printStackTrace();
        }
    }

    public Account get(Integer clientId, String accountName) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select get_account(?, ?) as account_id";
            CallableStatement statement = conn.prepareCall(sql);
            statement.setInt(1, clientId);
            statement.setString(2, accountName);
            ResultSet result = statement.executeQuery();
            result.next();
            Integer accountId = result.getInt("account_id");
            return get(accountId);
        } catch (SQLException e) {
            logger.error("error in database access when retrieving account by client and account name");
            e.printStackTrace();
            return null;
        }
    }

    public void approveAccount(Integer account_id) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "update accounts set account_status = 'APPROVED' where account_id = ?";
            PreparedStatement statement = conn.prepareCall(sql);
            statement.setInt(1, account_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("error in database access when approving account");
            e.printStackTrace();
        }
    }

    public void setBalance(Account account, double balance) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "update accounts set balance = ? where account_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setDouble(1, balance);
            statement.setInt(2, account.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("error in database access when updating balance");
            e.printStackTrace();
        }
    }
}
