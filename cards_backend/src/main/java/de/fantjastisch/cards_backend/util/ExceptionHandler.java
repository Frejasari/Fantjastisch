package de.fantjastisch.cards_backend.util;

import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.EntityDoesNotExistException;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorCode;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Collections;

/**
 * Exception Handler für die Api: fängt erwartete Exceptions ab und mappt sie auf erwartete Ausgaben.
 *
 * @author Freja Sender
 */
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({CommandValidationException.class})
    protected ResponseEntity<ErrorResponse> handleCommandValidationException(CommandValidationException e) {
        return new ResponseEntity(ErrorResponse.builder()
                .errors(e.getErrors()).build(),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({EntityDoesNotExistException.class})
    protected ResponseEntity<ErrorResponse> handleEntityDoesNotExiistException(EntityDoesNotExistException e) {
        return new ResponseEntity(ErrorResponse.builder()
                .errors(Collections.singletonList(
                        ErrorEntry.builder()
                                .field(e.getField())
                                .code(ErrorCode.ENTITY_DOES_NOT_EXIST)
                                .build())
                ).build(),
                HttpStatus.NOT_FOUND);
    }
}
