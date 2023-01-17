package de.fantjastisch.cards_backend.learningsystem.validator;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.CreateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.UpdateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import de.fantjastisch.cards_backend.util.validation.Validator;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.BOX_LABELS_IS_NULL_VIOLATION;

/**
 * Diese Klasse stellt die Erweiterung der Basis-Klasse {@link Validator} dar und führt weitere Prüfungen durch,
 * welche an die mit Link verbundenen Anwendungsfälle angepasst sind.
 *
 * @author Jessica Repty, Semjon Nirmann
 */
@Component
public class LearningSystemValidator extends Validator {

    private final LearningSystemQueryRepository learningSystemQueryRepository;

    @Autowired
    public LearningSystemValidator(LearningSystemQueryRepository learningSystemQueryRepository) {
        this.learningSystemQueryRepository = learningSystemQueryRepository;
    }

    /**
     * Diese Funktion validiert das Erstellen eines Lernsystems.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurde (ein Attribut ist leer oder null)
     * <p>
     *
     * @param command Eine {@link CreateLearningSystem}-Instanz, welche validiert werden soll.
     * @throws org.hibernate.tool.schema.spi.CommandAcceptanceException Constraint verletzt
     */
    public void validate(CreateLearningSystem command) {
        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateConstraints(command));
        throwIfNeeded(errors);
        errors.addAll(checkIfBoxLabelsContainNull(command.getBoxLabels()));
        throwIfNeeded(errors);
    }

    /**
     * Diese Funktion validiert das Aktualisieren eines Lernsystems.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurden (ein Attribut ist leer oder null),
     * und ob das zu aktualisierende Lernsystem nicht existiert,
     *
     * @param command Eine {@link UpdateLearningSystem}-Instanz, welche validiert werden soll.
     * @throws org.hibernate.tool.schema.spi.CommandAcceptanceException Constraint verletzt
     * @throws ResponseStatusException                                  wenn Lernsystem nicht existiert.
     */
    public void validate(UpdateLearningSystem command) {
        List<ErrorEntry> errors = new ArrayList<>();

        errors.addAll(validateConstraints(command));
        throwIfNeeded(errors);
        errors.addAll(checkIfBoxLabelsContainNull(command.getBoxLabels()));
        throwIfNeeded(errors);
        throwIfLearningSystemDoesNotExist(command.getId());
    }

    /**
     * Diese Funktion validiert das Lesen eines {@link LearningSystem}.
     * <p>
     * In diesem Rahmen wird geprüft, ob das zu lesende Lernsystem nicht existiert.
     *
     * @param learningSystemId Die ID des Lernsystems, welche geprüft werden soll.
     * @throws ResponseStatusException wenn Lernsystem nicht existiert.
     */
    public void validate(UUID learningSystemId) {
        throwIfLearningSystemDoesNotExist(learningSystemId);
    }


    private void throwIfLearningSystemDoesNotExist(UUID learningSystemId) {
        LearningSystem learningSystem = learningSystemQueryRepository.get(learningSystemId);
        if (learningSystem == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found LearningSystemId: " + learningSystemId
            );
        }
    }

    private List<ErrorEntry> checkIfBoxLabelsContainNull(List<String> labels) {
        List<ErrorEntry> errors = new ArrayList<>();
        if (labels.contains(null) || labels.contains("")) {
            errors.add(
                    ErrorEntry.builder()
                            .code(BOX_LABELS_IS_NULL_VIOLATION)
                            .field("boxLabels")
                            .build());
        }
        return errors;
    }
}
