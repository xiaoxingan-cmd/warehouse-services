package com.xiaoxingan.exceptions.products;

public class ProductsHaveDifferentCompanyNameException extends RuntimeException {
    public ProductsHaveDifferentCompanyNameException(String message) {
        super(message);
    }
}
