package com.xiaoxingan.exceptions.products;

public class ProductOrCustomerNotFoundException extends RuntimeException {
    public ProductOrCustomerNotFoundException(String message) {
        super(message);
    }
}
