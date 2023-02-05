package de.fantjastisch.cards_backend.learningsystem.aggregate;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemCommandRepository;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import de.fantjastisch.cards_backend.learningsystem.validator.LearningSystemValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Das LearningSystemAggregate stellt die Verbindung zwischen dem Controller und dem Persistance-Layer her, fungiert also
 * als Command-Handler für CRUD-Kommando-Objekte, welcher die eingehenden Kommandos vorher mit dem {@link LearningSystemValidator} validiert.
 *
 * @author Jessica Repty, Semjon Nirmann
 */
@Component
public class LearningSystemAggregate {
    private final LearningSystemCommandRepository learningSystemCommandRepository;
    private final LearningSystemValidator learningSystemValidator;
    private final LearningSystemQueryRepository learningSystemQueryRepository;
    private final UUIDGenerator uuidGenerator;

    @Autowired
    public LearningSystemAggregate(
            LearningSystemCommandRepository learningSystemCommandRepository,
            LearningSystemValidator learningSystemValidator,
            LearningSystemQueryRepository learningSystemQueryRepository, UUIDGenerator uuidGenerator) {
        this.learningSystemCommandRepository = learningSystemCommandRepository;
        this.learningSystemValidator = learningSystemValidator;
        this.learningSystemQueryRepository = learningSystemQueryRepository;
        this.uuidGenerator = uuidGenerator;
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Erstellen eines Lernsystems.
     *
     * @param command Das CRUD-Kommando-Objekt zum Erstellen eines Lernsystems.
     * @return Eine UUID, die die erstellte Entität darstellt.
     */
    public UUID handle(final CreateLearningSystem command) {
        learningSystemValidator.validate(command);

        LearningSystem learningSystem =
                LearningSystem.builder()
                        .id(uuidGenerator.randomUUID())
                        .label(command.getLabel().trim())
                        .boxLabels(command.getBoxLabels().stream().map(String::trim).toList())
                        .build();
        learningSystemCommandRepository.save(learningSystem);
        return learningSystem.getId();
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Aktualisieren eines Lernsystems.
     *
     * @param command Das CRUD-Kommando-Objekt zum Aktualisieren eines Lernsystems.
     */
    public void handle(final UpdateLearningSystem command) {
        learningSystemValidator.validate(command);

        learningSystemCommandRepository.update(
                LearningSystem.builder()
                        .id(command.getId())
                        .label(command.getLabel().trim())
                        .boxLabels(command.getBoxLabels().stream().map(String::trim).toList())
                        .build());
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Löschen eines Lernsystems.
     *
     * @param learningSystemId die Id des zu löschendne Lernsystems
     */
    public void handleDelete(final UUID learningSystemId) {
        learningSystemValidator.validate(learningSystemId);
        LearningSystem learningSystem = learningSystemQueryRepository.get(learningSystemId);
        learningSystemCommandRepository.delete(learningSystemId);
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Lesen eines Lernsystems.
     *
     * @param learningSystemId Die UUID eines Lernsystems, welche angefordert wird.
     * @return Die entsprechende Entität des Lernsystems, gekapselt in einer {@link LearningSystem}-Instanz.
     */
    public LearningSystem handleGet(final UUID learningSystemId) {
        learningSystemValidator.validate(learningSystemId);
        return learningSystemQueryRepository.get(learningSystemId);
    }

    /**
     * Diese Funktion liest alle Lernsysteme als Liste aus.
     *
     * @return Eine Liste aller Entitäten vom Typ Lernsystem, gekapselt in entsprechenden {@link LearningSystem}-Instanzen.
     */
    public List<LearningSystem> handle() {
        return learningSystemQueryRepository.getPage();
    }
}
