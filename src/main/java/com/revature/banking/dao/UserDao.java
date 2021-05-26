package com.revature.banking.dao;

import com.revature.banking.models.Client;
import com.revature.banking.services.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<Client, Integer>, Serializable {
    static Logger logger = LogManager.getLogger(UserDao.class);

    @Override
    public ArrayList<Client> getAll() {
        ArrayList<Client> users = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from clients";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                users.add(new Client(result.getString("username"),
                        result.getString("passwordhash"),
                        result.getInt("client_id")));
            }
            return users;
        } catch (SQLException e) {
            logger.error("error in database access when retrieving clients");
            return null;
        }
    }

    @Override
    public Client get(Integer id) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select * from clients where client_id = ?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            result.next();

            return new Client(result.getString("username"),
                    result.getString("passwordhash"),
                    result.getInt("client_id"));
        } catch (SQLException e) {
            logger.error("error in database access when retrieving client by id");
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
            return null;
        }
    }

    public Integer add(Client user) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "insert into clients(username, passwordhash) values(?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());

            statement.execute();
        } catch (SQLException e) {
            logger.error("error in database access when adding account");
        }
        return 0;
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
            e.printStackTrace();
        }
    }

    public void addAccountToUser(Integer accountId, Integer clientId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "insert into clients_accounts values(?, ?)";

            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, clientId);
            statement.setInt(2, accountId);

            statement.execute();
        } catch (SQLException e) {
            logger.error("error in database access when adding account-client relationship");
            e.printStackTrace();
        }
    }

    public List<Client> getAllAccountHolders(Integer accountId) {
        ArrayList<Client> clients = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select clients.username, clients.passwordhash, clients.client_id " +
                    "from ((clients_accounts " +
                    "inner join clients on clients.client_id = clients_accounts.client_id) " +
                    "inner join accounts on accounts.account_id = clients_accounts.account_id) " +
                    "where clients_accounts.account_id = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, accountId);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                clients.add(new Client(result.getString("username"),
                        result.getString("passwordhash"),
                        result.getInt("client_id")));
            }
            return clients;
        } catch (SQLException e) {
            logger.error("error in database access when retrieving accounts for client");
            return null;
        }
    }

    public void removeAccountAssociations(Integer clientId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "delete from clients_accounts where client_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, clientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("error in database access when removling client account associations");
        }
    }
}
