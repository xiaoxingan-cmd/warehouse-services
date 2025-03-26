package com.xiaoxingan.utils.validator;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

public class Validate<T> {
    @Inject
    Validator validator;

    @Getter
    private Response badResponse;

    public boolean validateData(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            Result result = new Result(violations);
            badResponse = Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(result, result.getMessage()))
                    .build();
            return false;
        } else {
            return true;
        }
    }
}
