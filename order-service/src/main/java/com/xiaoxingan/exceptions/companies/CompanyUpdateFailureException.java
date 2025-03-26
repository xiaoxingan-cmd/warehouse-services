package com.xiaoxingan.exceptions.companies;

public class CompanyUpdateFailureException extends RuntimeException {
    public CompanyUpdateFailureException(String message) {
        super(message);
    }
}
