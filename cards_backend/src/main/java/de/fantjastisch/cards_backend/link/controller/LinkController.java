package de.fantjastisch.cards_backend.link.controller;

import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.aggregate.CreateLink;
import de.fantjastisch.cards_backend.link.aggregate.DeleteLink;
import de.fantjastisch.cards_backend.link.aggregate.LinkAggregate;
import de.fantjastisch.cards_backend.link.aggregate.UpdateLink;
import de.fantjastisch.cards_backend.util.CreatedResponse;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
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
@Api(tags = {"Link"})
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
    // @ApiOperation -> io.swagger generiert ein Client
    @ApiOperation(
            value = "Create a new Link",
            notes = "Create a new Link",
            nickname = "createLink")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
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
    @ApiOperation(
            value = "Update a link",
            notes = "Update a link",
            nickname = "update")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
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
    @ApiOperation(
            value = "Delete a link",
            notes = "Delete a link",
            nickname = "delete")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
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
    @ApiOperation(
            value = "Get the Link from the given name and source",
            notes = "Get the Link from the given name and source",
            nickname = "getLink")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
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
    @ApiOperation(
            value = "Get all links",
            notes = "Get all links",
            nickname = "getList")
    @ApiResponses(
            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
    public List<Link> getList(@RequestParam UUID id) {
        return linkAggregate.handleGetAllLinksFromCard(id);
    }
}