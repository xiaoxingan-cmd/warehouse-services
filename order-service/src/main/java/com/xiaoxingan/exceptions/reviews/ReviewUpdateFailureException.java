package com.xiaoxingan.exceptions.reviews;

public class ReviewUpdateFailureException extends RuntimeException {
    public ReviewUpdateFailureException(String message) {
        super(message);
    }
}
