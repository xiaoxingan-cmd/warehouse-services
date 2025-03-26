package com.xiaoxingan.exceptions.orders;

public class OrderUpdateFailureException extends RuntimeException {
    public OrderUpdateFailureException(String message) {
        super(message);
    }
}
