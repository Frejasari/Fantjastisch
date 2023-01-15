package de.fantjastisch.cards_backend.link.controller;

import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.aggregate.CreateLink;
import de.fantjastisch.cards_backend.link.aggregate.DeleteLink;
import de.fantjastisch.cards_backend.link.aggregate.LinkAggregate;
import de.fantjastisch.cards_backend.link.aggregate.UpdateLink;
import de.fantjastisch.cards_backend.util.CreatedResponse;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry.mapErrorsToString;

/**
 * Diese Klasse stellt eine Schnittstelle zum Frontend dar und bietet API-Endpunkte, welche von der Außenwelt aufgerufen werden können.
 * <p>
 * Die gängigen CRUD-Operationen, Create Read Update Delete, werden hier zur Verfügung gestellt. Aufrufe werden an das
 * {@link LinkAggregate} weitergeleitet, es werden {@link CommandValidationException}-Instanzen abgefangen und nach außen
 * als {@link ResponseStatusException}-Objekte weitergegeben, zusammen mit den empfangenen Fehlern, welche von der
 * entsprechenden {@link CommandValidationException}-Instanz gekapselt werden.
 * <p>
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @Author Jessica Repty, Tamari Bayer
 */
@RestController
@Tag(name = "Link")
@RequestMapping("link")
public class LinkController {

    private final LinkAggregate linkAggregate;

    @Autowired
    public LinkController(LinkAggregate linkAggregate) {
        this.linkAggregate = linkAggregate;
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Erstellen einer Link-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link CreateLink}.
     * @return Eine Instanz der Klasse {@link CreatedResponse}.
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @PostMapping(path = "create", produces = "application/json")
    // @Operation -> io.swagger generiert ein Client
    @Operation(
            summary = "Create a new Link",
            operationId = "createLink")
    public CreatedResponse createLink(
            @RequestBody CreateLink command)
            throws RuntimeException {
        try {
            return new CreatedResponse(linkAggregate.handle(command));
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Aktualisieren einer Link-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link UpdateLink}.
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @PutMapping(path = "update")
    @Operation(
            summary = "Update a link",
            operationId = "updateLink")
    public void update(@RequestBody UpdateLink command) {
        try {
            linkAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Löschen einer Link-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link DeleteLink}.
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @DeleteMapping(path = "delete")
    @Operation(summary = "Delete a link",
            operationId = "deleteLink")
    public void delete(@RequestBody DeleteLink command) {
        try {
            linkAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }


    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen einer Link-Entität bereit.
     *
     * @param id Die UUID der Link-Entität, welche gelesen werden soll.
     * @return Eine Instanz der Klasse {@link Link}.
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @GetMapping(path = "get", produces = "application/json")
    @Operation(
            summary = "Get the Link from the given name and source",
            operationId = "getLink")
    public Link get(@RequestParam UUID id)
            throws RuntimeException {
        try {
            return linkAggregate.handleGetLink(id);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen entsprechender Link-Entitäten bereit.
     *
     * @param id Die UUID der {@link de.fantjastisch.cards_backend.card.Card}-Entität, von welcher die
     *           ausgehenden Links gelesen werden sollen.
     * @return Eine Liste von Instanzen der Klasse {@link Link}.
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @GetMapping(path = "getPage", produces = "application/json")
    @Operation(
            summary = "Get all links",
            operationId = "getPage")
    public List<Link> getPage(@RequestParam UUID id) {
        return linkAggregate.handleGetAllLinksFromCard(id);
    }
}