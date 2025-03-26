package com.xiaoxingan.exceptions.customers;

public class CustomerNotFoundException extends RuntimeException
{
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
