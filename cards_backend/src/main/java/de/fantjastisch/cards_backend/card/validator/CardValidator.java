package de.fantjastisch.cards_backend.card.validator;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.aggregate.CreateCard;
import de.fantjastisch.cards_backend.card.aggregate.UpdateCard;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.EntityDoesNotExistException;
import de.fantjastisch.cards_backend.util.validation.Validator;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.CARD_DUPLICATE_VIOLATION;
import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.CATEGORY_DOESNT_EXIST_VIOLATION;

/**
 * Diese Klasse stellt die Erweiterung der Basis-Klasse {@link Validator} dar und führt weitere
 * Prüfungen durch, welche an die mit Karteikarten verbundenen Anwendungsfälle angepasst sind.
 *
 * @author Tamari Bayer, Jessica Repty, Freja Sender
 */
@Component
public class CardValidator extends Validator {

    private final CardQueryRepository cardQueryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    public CardValidator(CardQueryRepository cardQueryRepository,
                         CategoryQueryRepository categoryQueryRepository) {
        this.cardQueryRepository = cardQueryRepository;
        this.categoryQueryRepository = categoryQueryRepository;
    }

    /**
     * Diese Funktion validiert das Erstellen einer Karteikarte.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurde (ein Attribut ist leer oder
     * null), ob alle Instanzen in der Kategorienliste Kategorien sind und ob es schon eine
     * Karteikarte mit den gleichen Attributen (Frage, Antwort, Tag und Kategorien) existiert.
     *
     * @param command Eine {@link CreateCard}-Instanz, welche validiert werden soll.
     * @throws CommandValidationException Constraint verletzt
     * @throws ResponseStatusException    Wenn eine Kategorie nicht existiert oder eine Karte mit
     *                                    gleichen Parametern schon vorhanden ist.
     */
    public void validate(final CreateCard command) {
        List<ErrorEntry> errors = new ArrayList<>();
        throwIfNeeded(validateConstraints(command));
        command.getLinks().forEach(link -> throwIfCardDoesNotExist(link.getTarget()));

        errors.addAll(checkIfCategoriesExist(command.getCategories()));
        errors.addAll(validateCardTaken(command.getQuestion(), command.getAnswer(), command.getTag()));
        throwIfNeeded(errors);
    }

    /**
     * Diese Funktion validiert das Aktualisieren einer Karteikarte.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurden (ein Attribut ist leer oder
     * null), ob alle Instanzen in der zu aktualisierenden Kategorienliste Kategorien sind und ob es
     * schon eine Karteikarte mit den gleichen Attributen (Frage, Antwort, Tag und Kategorien)
     * existiert.
     *
     * @param command Eine {@link UpdateCard}-Instanz, welche validiert werden soll.
     * @throws CommandValidationException Constraint verletzt
     * @throws ResponseStatusException    Wenn eine Kategorie nicht existiert oder eine Karte mit
     *                                    gleichen Parametern schon vorhanden ist.
     */
    public void validate(final UpdateCard command) {
        throwIfNeeded(validateConstraints(command));
        throwIfCardDoesNotExist(command.getId());
        command.getLinks().forEach(link -> throwIfCardDoesNotExist(link.getTarget()));

        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(checkIfCategoriesExist(command.getCategories()));
        throwIfNeeded(errors);
    }

    /**
     * Validiert ein Delete Command
     *
     * @param cardId die Id der zu löschenden Karte
     */
    public void validateDelete(final UUID cardId) {
        throwIfCardDoesNotExist(cardId);
    }

    /**
     * Valiediert ein Get Command
     *
     * @param cardId die Id der zu erhaltenen Karte
     */
    public void validateGet(final UUID cardId) {
        throwIfCardDoesNotExist(cardId);
    }

    private void throwIfCardDoesNotExist(final UUID cardId) {
        Card card = cardQueryRepository.get(cardId);
        if (card == null) {
            throw new EntityDoesNotExistException(cardId, "id");
        }
    }

    /**
     * Diese Funktion prüft, ob es schon eine Karteikarte mit den gleichen Attributen (Frage, Antwort,
     * Tag, Kategories) gibt.
     *
     * @param question Die zu überprüfende Frage.
     * @param answer   Die zu überprüfende Antwort.
     * @param tag      Der zu überprüfende Tag.
     * @return Die Liste aller Fehlermeldungen, die ermittelt wurden.
     */
    private List<ErrorEntry> validateCardTaken(final String question, final String answer, final String tag) {
        List<Card> cards = cardQueryRepository.getPage(null, null, null, false);
        List<Card> duplicateCard = cards.stream().filter
                        (card -> card.getQuestion().equals(question)
                                && card.getAnswer().equals(answer)
                                && card.getTag().equals(tag)
                        )
                .toList();
        if (!duplicateCard.isEmpty()) {
            return Collections.singletonList(
                    ErrorEntry.builder()
                            .code(CARD_DUPLICATE_VIOLATION)
                            .field("question")
                            .build());
        }
        return Collections.emptyList();
    }

    /**
     * Diese Funktion prüft die Existenz der an Karteikarten übergebenen Kategorien.
     *
     * @param categories Die Liste der UUIDs, die Instanzen von {@link Category} sein sollen.
     * @return Die Liste aller Fehlermeldungen, die ermittelt wurden.
     */
    private List<ErrorEntry> checkIfCategoriesExist(final Set<UUID> categories) {
        List<ErrorEntry> errors = new ArrayList<>();
        for (UUID category : categories) {
            if (categoryQueryRepository.get(category) == null) {
                errors.add(
                        ErrorEntry.builder()
                                .code(CATEGORY_DOESNT_EXIST_VIOLATION)
                                .field("categories")
                                .build());
            }
        }
        return errors;
    }
}
