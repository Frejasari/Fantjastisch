package de.fantjastisch.cards_backend.link.controller;

import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.aggregate.CreateLink;
import de.fantjastisch.cards_backend.link.aggregate.LinkAggregate;
import de.fantjastisch.cards_backend.link.aggregate.UpdateLink;
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
 * {@link LinkAggregate} weitergeleitet
 * <p>
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Jessica Repty, Tamari Bayer, Freja Sender
 */
@RestController
@Tag(name = "Link")
@RequestMapping("link")
@ApiResponses(value = {
        @ApiResponse(responseCode = "422", content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))})
})
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
     */

    @PostMapping(path = "create", produces = "application/json")
    // @Operation -> io.swagger generiert ein Client
    @Operation(
            summary = "Create a new Link",
            operationId = "createLink")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreatedResponse.class))})
    })
    public CreatedResponse createLink(
            @RequestBody CreateLink command)
            throws RuntimeException {
        return new CreatedResponse(linkAggregate.handle(command));
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Aktualisieren einer Link-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link UpdateLink}.
     */
    @PutMapping(path = "update")
    @Operation(
            summary = "Update a link",
            operationId = "updateLink")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public void update(@RequestBody UpdateLink command) {
        linkAggregate.handle(command);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Löschen einer Link-Entität bereit.
     *
     * @param id Eine Instanz der Klasse {@link DeleteLink}.
     */
    @DeleteMapping(path = "delete")
    @Operation(summary = "Delete a link",
            operationId = "deleteLink")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public void delete(@RequestParam UUID id) {
        linkAggregate.handleDelete(id);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen einer Link-Entität bereit.
     *
     * @param id Die UUID der Link-Entität, welche gelesen werden soll.
     * @return Eine Instanz der Klasse {@link Link}.
     */

    @GetMapping(path = "get", produces = "application/json")
    @Operation(
            summary = "Get the Link from the given name and source",
            operationId = "getLink")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Link.class))})
    })
    public Link get(@RequestParam UUID id)
            throws RuntimeException {
        return linkAggregate.handleGetLink(id);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen entsprechender Link-Entitäten bereit.
     *
     * @param id Die UUID der {@link de.fantjastisch.cards_backend.card.Card}-Entität, von welcher die
     *           ausgehenden Links gelesen werden sollen.
     * @return Eine Liste von Instanzen der Klasse {@link Link}.
     */
    @GetMapping(path = "getPage", produces = "application/json")
    @Operation(
            summary = "Get all links",
            operationId = "getPage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Link.class)))})
    })
    public List<Link> getPage(@RequestParam UUID id) {
        return linkAggregate.handleGetAllLinksFromCard(id);
    }
}