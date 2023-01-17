package de.fantjastisch.cards_backend.card.aggregate;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.repository.CardCommandRepository;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.card.validator.CardValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Das CardAggregate stellt die Verbindung zwischen dem Controller und dem Persistance-Layer her, fungiert also
 * als Command-Handler für CRUD-Kommando-Objekte, welcher die eingehenden Kommandos vorher mit dem {@link CardValidator} validiert.
 *
 * @author Tamari Bayer, Jessica Repty, Freja Sender
 */
@Component
public class CardAggregate {

    private final CardCommandRepository cardCommandRepository;
    private final CardQueryRepository cardQueryRepository;
    private final CardValidator cardValidator;
    private final UUIDGenerator uuidGenerator;

    @Autowired
    public CardAggregate(
            CardCommandRepository cardCommandRepository, CardQueryRepository cardQueryRepository,
            CardValidator cardValidator, UUIDGenerator uuidGenerator) {
        this.cardCommandRepository = cardCommandRepository;
        this.cardQueryRepository = cardQueryRepository;
        this.cardValidator = cardValidator;
        this.uuidGenerator = uuidGenerator;
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Erstellen einer Karteikarte.
     *
     * @param command Das CRUD-Kommando-Objekt zum Erstellen einer Karteikarte.
     * @return Eine UUID, die die erstellte Entität darstellt.
     */
    public UUID handle(final CreateCard command) {
        cardValidator.validate(command);

        de.fantjastisch.cards_backend.card.repository.Card newCard = de.fantjastisch.cards_backend.card.repository.Card.builder()
                .id(uuidGenerator.randomUUID())
                .question(command.getQuestion())
                .answer(command.getAnswer())
                .tag(command.getTag())
                .categories(command.getCategories())
                .build();
        cardCommandRepository.create(newCard);
        return newCard.getId();
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Lesen einer Karteikarte.
     *
     * @param cardId Die UUID einer Karteikarte, welche angefordert wird.
     * @return Die entsprechende Entität der Karteikarte, gekapselt in einer {@link Card}-Instanz.
     */
    public Card handle(final UUID cardId) {
        cardValidator.validateGet(cardId);
        return cardQueryRepository.get(cardId);
    }

    /**
     * Diese Funktion validiert und bearbeitet eine Anfrage zum Lesen aller Karteikarten {@link Card},
     * die nach entsprechendem Filtern gefunden werden.
     *
     * @param categoryFilter Die Liste der UUIDs der {@link de.fantjastisch.cards_backend.category.Category}-Entitäten,
     *                       wonach alle Karteikarten gefiltert werden sollen.
     * @param search         Ein String, wonach die Fragen und Antworten aller Karteikarten gefiltert werden.
     * @param tag            Ein String, wonach aller Karteikarten gefiltert werden.
     * @param sort           Ein Boolean, wenn er true ist, werden entsprechende Karteikarten alphabetisch nach Tags sortiert
     * @return Eine Liste der Instanzen der Klasse {@link Card}
     */
    public List<Card> handle(List<UUID> categoryFilter, String search, String tag, boolean sort) {
        return cardQueryRepository.getPage(categoryFilter, search, tag, sort);
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Löschen einer Karteikarte.
     *
     * @param cardId Die Id der zu löschenden Karte
     */
    public void handleDelete(final UUID cardId) {
        cardValidator.validateDelete(cardId);
        cardCommandRepository.delete(cardId);
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Aktualisieren einer Karteikarte.
     *
     * @param command Das CRUD-Kommando-Objekt zum Aktualisieren einer Karteikarte.
     */
    public void handle(final UpdateCard command) {

        cardValidator.validate(command);
        final de.fantjastisch.cards_backend.card.repository.Card card = de.fantjastisch.cards_backend.card.repository.Card.builder()
                .tag(command.getTag())
                .answer(command.getAnswer())
                .id(command.getId())
                .categories(command.getCategories())
                .question(command.getQuestion())
                .build();
        cardCommandRepository.update(card);
    }
}
