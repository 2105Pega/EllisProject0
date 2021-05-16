package com.revature.banking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User, String>, Serializable {
    private ArrayList<User> users;

    UserDao() {
        users = new ArrayList<User>();
    }

    @Override
    public ArrayList<User> getAll() {
        return users;
    }

    @Override
    public User get(String id) {
        for (User user: users) {
            if (user.getUsername().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void add(User user) {
        users.add(user);
    }
}
