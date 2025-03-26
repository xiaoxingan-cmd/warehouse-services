package com.xiaoxingan.exceptions.customers;

public class CustomerUpdateFailureException extends RuntimeException {
    public CustomerUpdateFailureException(String message) {
        super(message);
    }
}
