package de.fantjastisch.cards_backend.category.controller;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.aggregate.CategoryAggregate;
import de.fantjastisch.cards_backend.category.aggregate.CreateCategory;
import de.fantjastisch.cards_backend.category.aggregate.DeleteCategory;
import de.fantjastisch.cards_backend.category.aggregate.UpdateCategory;
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
 * {@link CategoryAggregate} weitergeleitet
 * <p>
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @author Semjon Nirmann, Freja Sender, Alexander Kück
 */
@RestController
@Tag(name = "Category")
@RequestMapping("category")
@ApiResponses(value = {
        @ApiResponse(responseCode = "422", content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class))})
})
public class CategoryController {

    private final CategoryAggregate categoryAggregate;

    @Autowired
    public CategoryController(CategoryAggregate categoryAggregate) {
        this.categoryAggregate = categoryAggregate;
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Erstellen einer Kategorien-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link CreateCategory}.
     * @return Eine Instanz der Klasse {@link CreatedResponse}.
     */
    @PostMapping(path = "create", produces = "application/json")
    @Operation(
            summary = "Create a new category",
            operationId = "createCategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreatedResponse.class))})
    })
    public CreatedResponse createCategory(
            @RequestBody CreateCategory command)
            throws RuntimeException {
        return new CreatedResponse(categoryAggregate.handle(command));
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Aktualisieren einer Kategorien-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link UpdateCategory}.
     */
    @PutMapping(path = "update")
    @Operation(
            summary = "Update a category",
            operationId = "updateCategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public void updateCategory(@RequestBody UpdateCategory command) {
        categoryAggregate.handle(command);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Löschen einer Kategorien-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link DeleteCategory}.
     */
    @DeleteMapping(path = "delete")
    @Operation(
            summary = "Delete a category",
            operationId = "deleteCategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public void deleteCategory(@RequestBody DeleteCategory command) {
        categoryAggregate.handle(command);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen einer Kategorien-Entität bereit.
     *
     * @param id Die UUID der Kategorien-Entität, welche gelesen werden soll.
     * @return Eine {@link Category}-Instanz, welche die angefragte Kategorie repräsentiert.
     */
    @GetMapping(path = "get", produces = "application/json")
    @Operation(
            summary = "Get specific category",
            operationId = "getCategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Category.class))})
    })
    public Category get(@RequestParam UUID id) {
        return categoryAggregate.handle(id);
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen aller Kategorien-Entitäten bereit.
     *
     * @return Eine Liste von {@link Category}-Instanzen.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Category.class)))})
    })
    @GetMapping(path = "getPage", produces = "application/json")
    @Operation(
            summary = "Get all categories",
            operationId = "getCategoryPage")
    public List<Category> getPage() {
        return categoryAggregate.handle();
    }


}
