package com.revature.banking.dao;

import java.util.ArrayList;
import java.util.List;

public interface Dao<T, T2> {
    List<T> getAll();

    T get(T2 id);

    T2 add(T newItem);

    void remove(T itemToRemove);
}
