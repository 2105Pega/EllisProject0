package com.revature.banking.exceptions;

public class TransactionFailedException extends Exception {
    public TransactionFailedException(String message) {
        super(message);
    }
}
