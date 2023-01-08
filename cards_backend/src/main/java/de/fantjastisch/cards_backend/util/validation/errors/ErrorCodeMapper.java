package de.fantjastisch.cards_backend.util.validation.errors;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodeMapper {

    private Map<Class, ErrorCode> constraintViolationMap =
            new HashMap<>() {
                {
                    put(NotNull.class, ErrorCode.NOT_NULL_VIOLATION);
                    put(NotBlank.class, ErrorCode.NOT_BLANK_VIOLATION);
                }
            };

    public ErrorCode mapErrorCode(final ConstraintViolation violation) {
        return mapErrorCode(violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    public ErrorCode mapErrorCode(final Class clazz) {
        ErrorCode error = constraintViolationMap.get(clazz);
        if (error == null) {
            return ErrorCode.CONSTRAINT_VIOLATION;
        } else {
            return error;
        }
    }
}
