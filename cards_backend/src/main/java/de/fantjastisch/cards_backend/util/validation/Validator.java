package de.fantjastisch.cards_backend.util.validation;

import de.fantjastisch.cards_backend.util.validation.errors.ErrorCodeMapper;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import jakarta.validation.ConstraintViolation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

/**
 * Basis-Klasse für die Validator-Klassen der unterschiedlichen CRUD - Kommandos der Modelle.
 * Validiert Annotationen der Modelle und wirft eine {@link CommandValidationException}, sofern entsprechende
 * {@link ErrorEntry}-Instanzen in einer Liste übergeben werden.
 *
 * @author Semjon Nirmann
 */
public class Validator {

    private final jakarta.validation.Validator annotationValidator = buildDefaultValidatorFactory().getValidator();

    private final ErrorCodeMapper mapper = new ErrorCodeMapper();

    /**
     * Eine Funktion zum Validieren der Annotationen bzw. Constraints von Kommando-Objekten.
     *
     * @param command Das Kommando-Objekt.
     * @param <T>     Der Typ-Parameter der Funktion, wobei T das Marker-Interface {@link Commandable} implementieren muss.
     * @return Eine Liste von {@link ErrorEntry}-Objekten, welche von {@link ConstraintViolation} zu konkreten {@link ErrorEntry}'s
     * umgewandelt werden.
     */
    public <T extends Commandable> List<ErrorEntry> validateConstraints(final T command) {
        final Set<ConstraintViolation<T>> constraintViolations = annotationValidator.validate(command);

        return constraintViolations.stream()
                .map(violation ->
                        ErrorEntry.builder()
                                .code(mapper.mapErrorCode(violation))
                                .field(violation.getPropertyPath().toString())
                                .build())
                .collect(Collectors.toList());
    }

    protected void throwIfNeeded(final List<ErrorEntry> errors) {
        if (!errors.isEmpty()) {
            throw new CommandValidationException(errors.toArray(ErrorEntry[]::new));
        }
    }
}