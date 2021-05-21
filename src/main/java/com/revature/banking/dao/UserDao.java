package com.revature.banking.dao;

import com.revature.banking.models.Account;
import com.revature.banking.models.Client;
import com.revature.banking.models.Transaction;
import com.revature.banking.models.User;
import com.revature.banking.services.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao implements Dao<Client, Integer>, Serializable {
    static Logger logger = LogManager.getLogger(TransactionDao.class);

    @Override
    public ArrayList<Client> getAll() {
        ArrayList<Client> users = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from transactions";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                users.add(new Client(result.getString("username"),
                        result.getString("passwordhash"),
                        result.getInt("client_id")));
            }
            return users;
        } catch (SQLException e) {
            logger.error("error in database access when retrieving transactions");
            return null;
        }
    }

    @Override
    public Client get(Integer id) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from accounts where account_id = ?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            result.next();

            return new Client(result.getString("username"),
                    result.getString("passwordhash"),
                    result.getInt("client_id"));
        } catch (SQLException e) {
            logger.error("error in database access when retrieving account by id");
            return null;
        }
    }

    public Client get(String username) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from clients where username = ?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Client(result.getString("username"),
                        result.getString("passwordhash"),
                        result.getInt("client_id"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("error in database access when retrieving client by username");
            e.printStackTrace();
            return null;
        }
    }

    public void add(Client user) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "insert into clients(username, passwordhash) values(?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());

            statement.execute();
        } catch (SQLException e) {
            logger.error("error in database access when adding account");
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Client client) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "delete from clients where client_id = ?";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setDouble(1, client.getId());

            statement.execute();
        } catch (SQLException e) {
            logger.error("error in database access when removing account");
        }
    }

    public void addAccountToUser(Integer accountId, Integer clientId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "insert into clients_account values(?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, clientId);
            statement.setInt(2, accountId);

            statement.execute();
        } catch (SQLException e) {
            logger.error("error in database access when adding account-client relationship");
        }
    }
}
