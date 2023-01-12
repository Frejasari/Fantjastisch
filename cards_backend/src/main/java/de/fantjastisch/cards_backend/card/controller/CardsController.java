package de.fantjastisch.cards_backend.card.controller;

import de.fantjastisch.cards_backend.card.aggregate.CardAggregate;
import de.fantjastisch.cards_backend.card.aggregate.CreateCard;
import de.fantjastisch.cards_backend.util.CreatedResponse;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry.mapErrorsToString;

/**
 * Diese Klasse stellt eine Schnittstelle zum Frontend dar und bietet API-Endpunkte, welche von der Außenwelt aufgerufen werden können.
 * <p>
 * Die gängigen CRUD-Operationen, Create Read Update Delete, werden hier zur Verfügung gestellt. Aufrufe werden an das
 * {@link CardAggregate} weitergeleitet, es werden {@link CommandValidationException}-Instanzen abgefangen und nach außen
 * als {@link ResponseStatusException}-Objekte weitergegeben, zusammen mit den empfangenen Fehlern, welche von der
 * entsprechenden {@link CommandValidationException}-Instanz gekapselt werden.
 * <p>
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @Author Tamari Bayer, Jessica Repty, Freja Sender
 */

@RestController
@Tag(name = "card")
@RequestMapping("card")
public class CardsController {

    private final CardAggregate cardAggregate;

    @Autowired
    public CardsController(CardAggregate cardAggregate) {
        this.cardAggregate = cardAggregate;
    }


    /**
     * Diese Funktion stellt den API-Endpunkt zum Erstellen einer Link-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link CreateCard}.
     * @return Eine Instanz der Klasse {@link CreatedResponse}.
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @PostMapping(path = "create", produces = "application/json")
    // @ApiOperation -> io.swagger generiert ein Client
    @Operation(
            summary = "Create a new Card",
            description = "Create a new Card")
    public CreatedResponse createCard(
            @RequestBody CreateCard command)
            throws RuntimeException {
        try {
            return new CreatedResponse(cardAggregate.handle(command));
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }
//
//    /**
//     * Diese Funktion stellt den API-Endpunkt zum Aktualisieren einer Link-Entität bereit.
//     *
//     * @param command Eine Instanz der Klasse {@link UpdateCard}.
//     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
//     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
//     */
//    @PutMapping(path = "update")
//    @ApiOperation(
//            value = "Update a card",
//            notes = "Update a card")
//    @ApiResponses(
//            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
//    public void updateCategory(@RequestBody UpdateCard command) throws RuntimeException {
//        try {
//            cardAggregate.handle(command);
//        } catch (CommandValidationException c) {
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
//        }
//    }
//
//    /**
//     * Diese Funktion stellt den API-Endpunkt zum Löschen einer Link-Entität bereit.
//     *
//     * @param command Eine Instanz der Klasse {@link DeleteCard}.
//     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
//     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
//     */
//    @DeleteMapping(path = "delete")
//    @ApiOperation(
//            value = "Delete a card",
//            notes = "Delete a card",
//            nickname = "delete")
//    @ApiResponses(
//            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
//    public void delete(@RequestBody DeleteCard command) throws RuntimeException {
//        try {
//            cardAggregate.handle(command);
//        } catch (CommandValidationException c) {
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
//        }
//    }
//
//    /**
//     * Diese Funktion stellt den API-Endpunkt zum Lesen entsprechender Link-Entitäten bereit.
//     *
//     * @param id Die UUID der {@link de.fantjastisch.cards_backend.card.Card}-Entität, die gelesen werden soll.
//     * @return Eine Instanz der Klasse {@link Card}.
//     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
//     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
//     */
//    @GetMapping(path = "get", produces = "application/json")
//    @ApiOperation(
//            value = "Get the Card from the given Id",
//            notes = "Get the Card from the given Id",
//            nickname = "getCard")
//    @ApiResponses(
//            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
//    public Card get(@RequestParam UUID id)
//            throws RuntimeException {
//        try {
//            return cardAggregate.handle(id);
//        } catch (CommandValidationException c) {
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
//        }
//    }
//
//    /**
//     * Diese Funktion stellt den API-Endpunkt zum Lesen aller oder nach Kategorien/Strings/Tags gefilterter Karteikarten-Entitäten bereit,
//     * die ggf. nach Tags sortiert werden können.
//     *
//     * @param categoryFilter Eine Liste der UUIDs der {@link de.fantjastisch.cards_backend.category.Category}-Entitäten,
//     *                       wonach alle Karteikarten gefiltert werden sollen.
//     * @param search         Ein String, wonach die Fragen und Antworten aller Karteikarten gefiltert werden.
//     * @param tag            Ein String, wonach aller Karteikarten gefiltert werden.
//     * @param sort           Ein Boolean, wenn er true ist, werden entsprechende Karteikarten alphabetisch nach Tags sortiert
//     * @return Eine Liste der Instanzen der Klasse {@link Card}
//     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
//     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
//     */
//    @GetMapping(path = "getList", produces = "application/json")
//    @ApiOperation(
//            value = "Get all cards",
//            notes = "Get all cards",
//            nickname = "getList")
//    @ApiResponses(
//            value = {@ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class)})
//    public List<Card> getPage(@RequestParam(required = false) List<UUID> categoryFilter,
//                              @RequestParam(required = false) String search,
//                              @RequestParam(required = false) String tag,
//                              @RequestParam(required = false) boolean sort) {
//        return cardAggregate.handle(categoryFilter, search, tag, sort);
//    }

}