package de.fantjastisch.cards_backend.util.validation;

import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommandValidationException extends RuntimeException{

    private final transient List<ErrorEntry> errors;

    public CommandValidationException(final ErrorEntry... errors) {
        this.errors = Arrays.stream(errors).collect(Collectors.toList());
    }
}
