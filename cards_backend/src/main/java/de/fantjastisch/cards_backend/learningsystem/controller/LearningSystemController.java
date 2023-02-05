package de.fantjastisch.cards_backend.learningsystem.controller;

import de.fantjastisch.cards_backend.learningsystem.LearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.CreateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.LearningSystemAggregate;
import de.fantjastisch.cards_backend.learningsystem.aggregate.UpdateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
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
 * {@link LearningSystemAggregate} weitergeleitet
 * <p>
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Semjon, Nirmann, Jessica Repty, Alex Kück, Freja Sender
 */
@RestController
@Tag(name = "LearningSystem")
@RequestMapping("learningSystem")
@ApiResponses(value = {
        @ApiResponse(responseCode = "422", content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))})
})
public class LearningSystemController {

    private final LearningSystemAggregate learningSystemAggregate;

    @Autowired
    public LearningSystemController(LearningSystemAggregate learningSystemAggregate,
                                    LearningSystemQueryRepository learningSystemQueryRepository) {
        this.learningSystemAggregate = learningSystemAggregate;
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Erstellen einer Lernsystem-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link CreateLearningSystem}.
     * @return Eine Instanz der Klasse {@link CreatedResponse}.
     */
    @PostMapping(path = "create", produces = "application/json")
    @Operation(
            summary = "Create a new learning system",
            operationId = "createLearningSystem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreatedResponse.class))})
    })
    public CreatedResponse createLearningSystem(
            @RequestBody CreateLearningSystem command)
            throws RuntimeException {
        return new CreatedResponse(learningSystemAggregate.handle(command));
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Aktualisieren einer Lernsystem-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link UpdateLearningSystem}.
     */
    @PutMapping(path = "update")
    @Operation(
            summary = "Update a learning system",
            operationId = "updateLearningSystem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public void updateLearningSystem(@RequestBody UpdateLearningSystem command) {
        learningSystemAggregate.handle(command);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Löschen einer Lernsystem-Entität bereit.
     *
     * @param id Die Id des zu löschenden Lernsystems
     */
    @DeleteMapping(path = "delete")
    @Operation(
            summary = "Delete a learning system",
            operationId = "deleteLearningSystem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public void deleteLearningSystem(@RequestParam UUID id) {
        learningSystemAggregate.handleDelete(id);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen einer Lernsystem-Entität bereit.
     *
     * @param id Die UUID der Lernsystem-Entität, welche gelesen werden soll.
     * @return Eine Instanz der Klasse {@link LearningSystem}.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = LearningSystem.class))})
    })
    @GetMapping(path = "get", produces = "application/json")
    @Operation(
            summary = "Get specific learning system",
            operationId = "getLearningSystem")
    public LearningSystem get(@RequestParam UUID id) {
        return learningSystemAggregate.handleGet(id);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen aller Lernsystem-Entitäten bereit.
     *
     * @return Eine Liste von Instanzen der Klasse {@link LearningSystem}.
     */
    @GetMapping(path = "getPage", produces = "application/json")
    @Operation(
            summary = "Get all learning systems",
            operationId = "getLearningSystemList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = LearningSystem.class)))})
    })
    public List<LearningSystem> getPage() {
        return learningSystemAggregate.handle();
    }
}
