package com.xiaoxingan.exceptions.products;

public class ProductHasRunOutException extends RuntimeException {
    public ProductHasRunOutException(String message) {
        super(message);
    }
}
