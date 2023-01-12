package de.fantjastisch.cards_backend.card.validator;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.aggregate.CreateCard;
import de.fantjastisch.cards_backend.card.aggregate.DeleteCard;
import de.fantjastisch.cards_backend.card.aggregate.UpdateCard;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.Validator;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;

/**
 * Diese Klasse stellt die Erweiterung der Basis-Klasse {@link Validator} dar und führt weitere Prüfungen durch,
 * welche an die mit Karteikarten verbundenen Anwendungsfälle angepasst sind.
 *
 * @Author Tamari Bayer, Jessica Repty, Freja Sender
 */
@Component
public class CardValidator extends Validator {

    private final CardQueryRepository cardQueryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    public CardValidator(CardQueryRepository cardQueryRepository, CategoryQueryRepository categoryQueryRepository) {
        this.cardQueryRepository = cardQueryRepository;
        this.categoryQueryRepository = categoryQueryRepository;
    }

    /**
     * Diese Funktion validiert das Erstellen einer Karteikarte.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurde (ein Attribut ist leer oder null),
     * ob alle Instanzen in der Kategorienliste Kategorien sind und ob es schon eine Karteikarte mit den
     * gleichen Attributen (Frage, Antwort, Tag und Kategorien) existiert.
     *
     * @param command Eine {@link CreateCard}-Instanz, welche validiert werden soll.
     * @throws CommandValidationException Constraint verletzt
     * @throws ResponseStatusException    Wenn eine Kategorie nicht existiert oder eine Karte mit gleichen Parametern
     *                                    schon vorhanden ist.
     */
    public void validate(CreateCard command) {
        throwIfNeeded(validateConstraints(command));

        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(checkIfCategoriesExist(command.getCategories()));
        errors.addAll(validateCardTaken(command.getQuestion(), command.getAnswer(), command.getTag(), command.getCategories(),
                cardQueryRepository.getPage(null, null, null, false)));
        throwIfNeeded(errors);
    }

    /**
     * Diese Funktion validiert das Aktualisieren einer Karteikarte.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurden (ein Attribut ist leer oder null),
     * ob alle Instanzen in der zu aktualisierenden Kategorienliste Kategorien sind und ob es schon eine Karteikarte mit den
     * gleichen Attributen (Frage, Antwort, Tag und Kategorien) existiert.
     *
     * @param command Eine {@link UpdateCard}-Instanz, welche validiert werden soll.
     * @throws CommandValidationException Constraint verletzt
     * @throws ResponseStatusException    Wenn eine Kategorie nicht existiert oder eine Karte mit gleichen Parametern
     *                                    schon vorhanden ist.
     */
    public void validate(UpdateCard command) {
        throwIfNeeded(validateConstraints(command));
        throwIfCardDoesNotExist(command.getId());

        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(checkIfCategoriesExist(command.getCategories()));
        throwIfNeeded(errors);
    }

    public void validate(DeleteCard command) {
        throwIfNeeded(validateConstraints(command));
        throwIfCardDoesNotExist(command.getId());
    }

    public void validateGet(UUID cardId) {
        throwIfCardDoesNotExist(cardId);
    }

    private void throwIfCardDoesNotExist(final UUID cardId) {
        Card card = cardQueryRepository.get(cardId);
        if (card == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found"
            );
        }
    }

    /**
     * Diese Funktion prüft, ob es schon eine Karteikarte mit den gleichen Attributen (Frage, Antwort, Tag, Kategories) gibt.
     *
     * @param question   Die zu überprüfende Frage.
     * @param answer     Die zu überprüfende Antwort.
     * @param tag        Der zu überprüfende Tag.
     * @param categories Die zu überprüfende Liste von UUIDs, die Instanzen {@link Category} sind.
     * @param cards      Die Liste aller Karteikarten.
     * @return Die Liste aller Fehlermeldungen, die ermittelt wurden.
     */
    private List<ErrorEntry> validateCardTaken(String question, String answer, String tag, List<UUID> categories, List<Card> cards) {
        List<Card> duplicateCard = cards.stream().filter
                (card -> card.getQuestion().equals(question)
                        && card.getAnswer().equals(answer)
                        && card.getTag().equals(tag)
                        && card.getCategories().equals(categories)).toList();
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
    private List<ErrorEntry> checkIfCategoriesExist(List<UUID> categories) {
        List<ErrorEntry> errors = new ArrayList<>();
        if (categories.isEmpty()) {
            errors.add(
                    ErrorEntry.builder()
                            .code(NO_CATEGORIES_VIOLATION)
                            .field("categories")
                            .build());
        }
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
