package de.fantjastisch.cards_backend.link.aggregate;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.repository.LinkCommandRepository;
import de.fantjastisch.cards_backend.link.repository.LinkQueryRepository;
import de.fantjastisch.cards_backend.link.validator.LinkValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Das LinkAggregate stellt die Verbindung zwischen dem Controller und dem Persistance-Layer her, fungiert also
 * als Command-Handler für CRUD-Kommando-Objekte, welcher die eingehenden Kommandos vorher mit dem {@link LinkValidator} validiert.
 *
 * @author Jessica Repty, Tamari Bayer
 */
@Component
public class LinkAggregate {

    private final LinkCommandRepository linkCommandRepository;
    private final LinkQueryRepository linkQueryRepository;
    private final LinkValidator linkValidator;
    private final UUIDGenerator uuidGenerator;


    @Autowired
    public LinkAggregate(LinkCommandRepository linkCommandRepository, LinkQueryRepository linkQueryRepository,
                         LinkValidator linkValidator, UUIDGenerator uuidGenerator) {
        this.linkCommandRepository = linkCommandRepository;
        this.linkQueryRepository = linkQueryRepository;
        this.linkValidator = linkValidator;
        this.uuidGenerator = uuidGenerator;
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Erstellen eines Links.
     *
     * @param command Das CRUD-Kommando-Objekt zum Erstellen eines Links.
     * @return Eine UUID, die die erstellte Entität darstellt.
     */
    public UUID handle(final CreateLink command) {
        linkValidator.validate(command);

        Link newLink = Link.builder()
                .id(uuidGenerator.randomUUID())
                .name(command.getName())
                .source(command.getSource())
                .target(command.getTarget())
                .build();
        linkCommandRepository.save(newLink);
        return newLink.getId();
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Löschen eines Links.
     *
     * @param linkID Das CRUD-Kommando-Objekt zum Löschen eines Links.
     */
    public void handleDelete(final UUID linkID) {
        linkValidator.validateLink(linkID);
        linkCommandRepository.delete(linkID);
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Aktualisieren eines Links.
     *
     * @param command Das CRUD-Kommando-Objekt zum Aktualisieren eines Links.
     */
    public void handle(final UpdateLink command) {
        linkValidator.validate(command);
        Link updatedLink = Link.builder()
                .name(command.getName())
                .source(command.getSource())
                .target(command.getTarget())
                .build();

        linkCommandRepository.update(updatedLink);
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Lesen eines Links.
     *
     * @param linkId Die UUID eines Links, welche angefordert wird.
     * @return Die entsprechende Entität des Links, gekapselt in einer {@link Link}-Instanz.
     */
    public Link handleGetLink(final UUID linkId) {
        linkValidator.validateLink(linkId);
        return linkQueryRepository.get(linkId);
    }

    /**
     * Diese Funktion validiert und bearbeitet eine Anfrage zum Lesen aller ausgehenden Links einer {@link Card}.
     *
     * @param sourceId Die UUID einer {@link Card}, von welcher die ausgehenden Links angefordert werden.
     * @return Eine Liste der entsprechenden Entitäten vom Typ Link, gekapselt in entsprechenden {@link Link}-Instanzen.
     */
    public List<Link> handleGetAllLinksFromCard(final UUID sourceId) {
        linkValidator.validateCard(sourceId);
        return linkQueryRepository.getPage(sourceId);
    }

}

