package de.fantjastisch.cards_backend.link.validator;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.aggregate.CreateLink;
import de.fantjastisch.cards_backend.link.aggregate.UpdateLink;
import de.fantjastisch.cards_backend.link.repository.LinkQueryRepository;
import de.fantjastisch.cards_backend.util.validation.Validator;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt die Erweiterung der Basis-Klasse {@link Validator} dar und führt weitere Prüfungen durch,
 * welche an die mit Link verbundenen Anwendungsfälle angepasst sind.
 *
 * @author Jessica Repty, Tamari Bayer
 */
@Component
public class LinkValidator extends Validator {

    final private LinkQueryRepository linkQueryRepository;
    final private CardQueryRepository cardQueryRepository;

    public LinkValidator(LinkQueryRepository linkQueryRepository, CardQueryRepository cardQueryRepository) {
        this.linkQueryRepository = linkQueryRepository;
        this.cardQueryRepository = cardQueryRepository;
    }

    /**
     * Diese Funktion validiert das Erstellen eines Links.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurde (ein Attribut ist leer oder null),
     * und ob die beiden Instanzen von {@link Card}, die übergeben werden, nicht existieren.
     * <p>
     *
     * @param command Eine {@link CreateLink}-Instanz, welche validiert werden soll.
     * @throws org.hibernate.tool.schema.spi.CommandAcceptanceException Constraint verletzt
     * @throws ResponseStatusException                                  wenn Link oder Karte nicht existieren.
     */
    public void validate(CreateLink command) {
        List<ErrorEntry> errors = new ArrayList<>();

        errors.addAll(validateConstraints(command));
        throwIfNeeded(errors);

        throwIfCardDoesNotExist(command.getSource());
        throwIfCardDoesNotExist(command.getTarget());
    }

    /**
     * Diese Funktion validiert das Aktualisieren eines Links.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurden (ein Attribut ist leer oder null),
     * ob der zu aktualisierende Link nicht existiert,
     * ob die beiden Instanzen von {@link Card}, die übergeben werden, nicht existieren.
     *
     * @param command Eine {@link UpdateLink}-Instanz, welche validiert werden soll.
     * @throws org.hibernate.tool.schema.spi.CommandAcceptanceException Constraint verletzt
     * @throws ResponseStatusException                                  wenn Link oder Karte nicht existieren.
     */
    public void validate(UpdateLink command) {
        List<ErrorEntry> errors = new ArrayList<>();

        errors.addAll(validateConstraints(command));
        throwIfNeeded(errors);

        throwIfLinkDoesNotExist(command.getId());
        throwIfCardDoesNotExist(command.getSource());
        throwIfCardDoesNotExist(command.getTarget());
    }

    /**
     * Diese Funktion validiert das Lesen einer {@link Card}.
     * <p>
     * In diesem Rahmen wird geprüft, ob der zu lesende Link nicht existiert.
     *
     * @param cardId Die ID der Karteikarte, welche geprüft werden soll.
     * @throws ResponseStatusException wenn Karte nicht existiert.
     */
    public void validateCard(UUID cardId) {
        throwIfCardDoesNotExist(cardId);
    }

    /**
     * Diese Funktion validiert das Lesen eines Links.
     * <p>
     * In diesem Rahmen wird geprüft, ob der zu lesende Link nicht existiert.
     *
     * @param linkId Die ID des Links, welche geprüft werden soll.
     * @throws ResponseStatusException wenn Link nicht existiert.
     */
    public void validateLink(UUID linkId) {
        throwIfLinkDoesNotExist(linkId);
    }


    private void throwIfCardDoesNotExist(UUID cardId) {
        Card card = cardQueryRepository.get(cardId);
        if (card == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found CardId: " + cardId
            );
        }
    }

    private void throwIfLinkDoesNotExist(final UUID linkId) {
        Link link = linkQueryRepository.get(linkId);
        if (link == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found LinkId: " + linkId
            );
        }
    }
}


