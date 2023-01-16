package de.fantjastisch.cards_backend.card.controller;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.aggregate.CardAggregate;
import de.fantjastisch.cards_backend.card.aggregate.CreateCard;
import de.fantjastisch.cards_backend.card.aggregate.DeleteCard;
import de.fantjastisch.cards_backend.card.aggregate.UpdateCard;
import de.fantjastisch.cards_backend.util.CreatedResponse;
import de.fantjastisch.cards_backend.util.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Diese Klasse stellt eine Schnittstelle zum Frontend dar und bietet API-Endpunkte, welche von der Außenwelt aufgerufen werden können.
 * <p>
 * Die gängigen CRUD-Operationen, Create Read Update Delete, werden hier zur Verfügung gestellt. Aufrufe werden an das
 * {@link CardAggregate} weitergeleitet
 * <p>
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Tamari Bayer, Jessica Repty, Freja Sender
 */

@RestController
@Tag(name = "card")
@RequestMapping("card")
@ApiResponses(value = {
        @ApiResponse(responseCode = "422", content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))})
})
public class CardController {

    private final CardAggregate cardAggregate;

    @Autowired
    public CardController(CardAggregate cardAggregate) {
        this.cardAggregate = cardAggregate;
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Erstellen einer Link-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link CreateCard}.
     * @return Eine Instanz der Klasse {@link CreatedResponse}.
     */
    @PostMapping(path = "create", produces = "application/json")
    // @Operation -> io.swagger generiert ein Client
    @Operation(description = "Create a new Card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreatedResponse.class))})
    })
    public CreatedResponse createCard(
            @RequestBody CreateCard command)
            throws RuntimeException {
        return new CreatedResponse(cardAggregate.handle(command));
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Aktualisieren einer Link-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link UpdateCard}.
     */
    @PutMapping(path = "update")
    @Operation(description = "Update a card",
            operationId = "updateCard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public void updateCard(@RequestBody UpdateCard command) throws RuntimeException {
        cardAggregate.handle(command);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Löschen einer Link-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link DeleteCard}.
     */
    @DeleteMapping(path = "delete")
    @Operation(
            description = "Delete a card",
            operationId = "deleteCard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public void delete(@RequestBody DeleteCard command) throws RuntimeException {
        cardAggregate.handle(command);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen entsprechender Link-Entitäten bereit.
     *
     * @param id Die UUID der {@link de.fantjastisch.cards_backend.card.Card}-Entität, die gelesen werden soll.
     * @return Eine Instanz der Klasse {@link Card}.
     */
    @GetMapping(path = "get", produces = "application/json")
    @Operation(
            description = "Get the Card from the given Id",
            operationId = "getCard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Card.class))})
    })
    public Card get(@RequestParam UUID id)
            throws RuntimeException {
        return cardAggregate.handle(id);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen aller oder nach Kategorien/Strings/Tags gefilterter Karteikarten-Entitäten bereit,
     * die ggf. nach Tags sortiert werden können.
     *
     * @param categoryFilter Eine Liste der UUIDs der {@link de.fantjastisch.cards_backend.category.Category}-Entitäten,
     *                       wonach alle Karteikarten gefiltert werden sollen.
     * @param search         Ein String, wonach die Fragen und Antworten aller Karteikarten gefiltert werden.
     * @param tag            Ein String, wonach aller Karteikarten gefiltert werden.
     * @param sort           Ein Boolean, wenn er true ist, werden entsprechende Karteikarten alphabetisch nach Tags sortiert
     * @return Eine Liste der Instanzen der Klasse {@link Card}
     */
    @GetMapping(path = "getPage", produces = "application/json")
    @Operation(
            summary = "Get all cards",
            operationId = "getCardPage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Card.class)))})
    })
    public List<Card> getPage(@RequestParam(required = false) List<UUID> categoryFilter,
                              @RequestParam(required = false) String search,
                              @RequestParam(required = false) String tag,
                              @RequestParam(required = false) boolean sort) {
        return cardAggregate.handle(categoryFilter, search, tag, sort);
    }
}