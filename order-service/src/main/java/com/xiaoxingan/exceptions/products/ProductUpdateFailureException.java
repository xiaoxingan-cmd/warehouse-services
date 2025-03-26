package com.xiaoxingan.exceptions.products;

public class ProductUpdateFailureException extends RuntimeException {
    public ProductUpdateFailureException(String message) {
        super(message);
    }
}
