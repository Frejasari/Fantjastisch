package de.fantjastisch.cards_backend.util.validation.errors;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Hilfsklasse welche im Rahmen der Constraint-Validierung von CRUD-Kommando-Objekten verwendet wird.
 * Bildet Annotations-Klassen auf selbst-definierte {@link ErrorCode} Enums ab.
 *
 * @author Semjon Nirmann
 */
public class ErrorCodeMapper {

    private Map<Class, ErrorCode> constraintViolationMap =
            new HashMap<>() {
                {
                    put(NotNull.class, ErrorCode.NOT_NULL_VIOLATION);
                    put(NotBlank.class, ErrorCode.NOT_BLANK_VIOLATION);
                }
            };

    /**
     * Bildet eine {@link ConstraintViolation} auf einen {@link ErrorCode} ab.
     *
     * @param violation Die abzubildende {@link ConstraintViolation}.
     * @return Eine {@link ErrorCode}-Instanz.
     */
    public ErrorCode mapErrorCode(final ConstraintViolation violation) {
        return mapErrorCode(violation.getConstraintDescriptor().getAnnotation().annotationType());
    }

    /**
     * Bildet eine Annotations-Klasse auf eine {@link ErrorCode}-Instanz ab.
     *
     * @param clazz Die Annotations-Klasse, welche abgebildet werden soll.
     * @return Die {@link ErrorCode}-Instanz.
     */
    public ErrorCode mapErrorCode(final Class clazz) {
        ErrorCode error = constraintViolationMap.get(clazz);
        if (error == null) {
            return ErrorCode.CONSTRAINT_VIOLATION;
        } else {
            return error;
        }
    }
}
