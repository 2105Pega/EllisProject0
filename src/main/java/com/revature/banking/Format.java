package com.revature.banking;

public class Format {
    public static String f(double amount) {
        return String.format("$%.2f", amount);
    }
}
