package com.xiaoxingan.exceptions.orders;

public class ConstructOrderFailureException extends RuntimeException {
    public ConstructOrderFailureException(String message) {
        super(message);
    }
}
