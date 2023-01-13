package de.fantjastisch.cards_backend.category.controller;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.aggregate.CategoryAggregate;
import de.fantjastisch.cards_backend.category.aggregate.CreateCategory;
import de.fantjastisch.cards_backend.category.aggregate.DeleteCategory;
import de.fantjastisch.cards_backend.category.aggregate.UpdateCategory;
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
 * {@link CategoryAggregate} weitergeleitet, es werden {@link CommandValidationException}-Instanzen abgefangen und nach außen
 * als {@link ResponseStatusException}-Objekte weitergegeben, zusammen mit den empfangenen Fehlern, welche von der
 * entsprechenden {@link CommandValidationException}-Instanz gekapselt werden.
 * <p>
 * Eine Instanz dieser Klasse wird als Parameter vom entsprechenden API-Endpunkt entgegengenommen.
 *
 * @Author Semjon Nirmann, Alexander Kück
 */
@RestController
@Tag(name = "Category")
@RequestMapping("category")
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
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @PostMapping(path = "create", produces = "application/json")
    @Operation(
            summary = "Create a new category",
            operationId = "createCategory")
    public CreatedResponse createCategory(
            @RequestBody CreateCategory command)
            throws RuntimeException {
        try {
            return new CreatedResponse(categoryAggregate.handle(command));
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Aktualisieren einer Kategorien-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link UpdateCategory}.
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @PutMapping(path = "update")
    @Operation(
            summary = "Update a category",
            operationId = "updateCategory")
    public void updateCategory(@RequestBody UpdateCategory command) {
        try {
            categoryAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Löschen einer Kategorien-Entität bereit.
     *
     * @param command Eine Instanz der Klasse {@link DeleteCategory}.
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @DeleteMapping(path = "delete")
    @Operation(
            summary = "Delete a category",
            operationId = "deleteCategory")
    public void deleteCategory(@RequestBody DeleteCategory command) {
        try {
            categoryAggregate.handle(command);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen einer Kategorien-Entität bereit.
     *
     * @param id Die UUID der Kategorien-Entität, welche gelesen werden soll.
     * @return Eine {@link Category}-Instanz, welche die angefragte Kategorie repräsentiert.
     * @throws RuntimeException Eine {@link ResponseStatusException}, welche Auskunft über Fehlermeldungen gibt,
     *                          die während der Validierung des Kommandos entstanden sind und den entsprechenden HTTP-Status-Code ausgibt.
     */
    @GetMapping(path = "get", produces = "application/json")
    @Operation(
            summary = "Get specific category",
            operationId = "getCategory")
    public Category get(@RequestParam UUID id) {
        try {
            return categoryAggregate.handle(id);
        } catch (CommandValidationException c) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, mapErrorsToString(c.getErrors()));
        }
    }

    /**
     * Diese Funktion stellt den API-Endpunkt zum Lesen aller Kategorien-Entitäten bereit.
     *
     * @return Eine Liste von {@link Category}-Instanzen.
     */
    @GetMapping(path = "getPage", produces = "application/json")
    @Operation(
            summary = "Get all categories",
            operationId = "getCategoryPage")
    public List<Category> getPage() {
        return categoryAggregate.handle();
    }


}
