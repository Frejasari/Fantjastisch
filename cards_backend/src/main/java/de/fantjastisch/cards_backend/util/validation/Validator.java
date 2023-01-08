package de.fantjastisch.cards_backend.util.validation;

import de.fantjastisch.cards_backend.util.validation.errors.ErrorCodeMapper;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Validator {

    private
    jakarta.validation.Validator annotationValidator =
            Validation.buildDefaultValidatorFactory().getValidator();

    private ErrorCodeMapper mapper = new ErrorCodeMapper();

    public <T extends Commandable> List<ErrorEntry> validateConstraints(final T command) {
        final Set<ConstraintViolation<T>> constraintViolations = annotationValidator.validate(command);

        return constraintViolations.stream()
                .map(violation ->
                        ErrorEntry.builder()
                                .code(mapper.mapErrorCode(violation))
                                .field(violation.getPropertyPath().toString())
                                .violation(violation)
                                .build())
                .collect(Collectors.toList());
    }

    protected void throwIfNeeded(final List<ErrorEntry> errors) {
        if (!errors.isEmpty()) {
            throw new CommandValidationException(errors.toArray(ErrorEntry[]::new));
        }
    }
}
