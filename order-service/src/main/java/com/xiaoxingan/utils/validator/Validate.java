package com.xiaoxingan.utils.validator;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

@Slf4j
public class Validate<T> {
    @Inject
    Validator validator;

    @Getter
    private Response badResponse;

    public boolean validateData(T object) {
        log.debug("Валидирую данные DTO объекта...");
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            log.warn("DTO не прошел валидацию!");
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
