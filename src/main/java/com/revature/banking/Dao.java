package com.revature.banking;

import java.util.ArrayList;
import java.util.List;

public interface Dao<T, T2> {
    ArrayList<T> getAll();

    T get(T2 id);

    void add(T newItem);
}
