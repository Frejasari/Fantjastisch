package de.fantjastisch.cards_backend.category.validator;

import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.aggregate.CreateCategory;
import de.fantjastisch.cards_backend.category.aggregate.DeleteCategory;
import de.fantjastisch.cards_backend.category.aggregate.UpdateCategory;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.util.validation.Validator;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;

/**
 * Diese Klasse stellt die Erweiterung der Basis-Klasse {@link Validator} dar und führt weitere Prüfungen durch,
 * welche an die mit Kategorien verbundenen Anwendungsfälle angepasst sind.
 *
 * @Author Semjon Nirmann
 */
@Component
public class CategoryValidator extends Validator {

    private final CardQueryRepository cardQueryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    @Autowired
    public CategoryValidator(CardQueryRepository cardQueryRepository, CategoryQueryRepository categoryQueryRepository) {
        this.cardQueryRepository = cardQueryRepository;
        this.categoryQueryRepository = categoryQueryRepository;
    }

    /**
     * Diese Funktion validiert das Erstellen einer Kategorie.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurden (ein Attribut ist leer oder null),
     * ob das Label der Kategorie ggf. bereits vergeben ist, und ob die Unterkategorien, die übergeben werden,
     * nicht existieren.
     * <p>
     * Es wird eine {@link de.fantjastisch.cards_backend.util.validation.CommandValidationException} geworfen, sofern einer dieser Fälle gilt.
     *
     * @param command Eine {@link CreateCategory}-Instanz, welche validiert werden soll.
     */
    public void validate(CreateCategory command) {
        throwIfNeeded(validateConstraints(command));
        throwIfNeeded(checkIfSubcategoriesContainNull(command.getSubCategories()));

        List<ErrorEntry> errors = new ArrayList<>();
        final List<Category> allCategories = categoryQueryRepository.getPage();
        errors.addAll(checkIfLabelTaken(command.getLabel(), allCategories));
        errors.addAll(checkIfSubcategoryExists(command.getSubCategories(), allCategories));
        throwIfNeeded(errors);
    }

    /**
     * Diese Funktion validiert das Aktualisieren einer Kategorie.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurden (ein Attribut ist leer oder null),
     * ob die zu aktualisierende Kategorie nicht existiert,
     * ob das Label der Kategorie ggf. bereits vergeben ist,
     * und ob die Unterkategorien, die übergeben werden, nicht existieren.
     * Es wird außerdem geprüft, ob Kreise in der Kategorien-Hierarchie entstehen, wenn die Unterkategorien der übergebenen
     * Instanz eingefügt werden.
     * Es wird eine {@link de.fantjastisch.cards_backend.util.validation.CommandValidationException} bzw.
     * eine {@link ResponseStatusException} geworfen, sofern einer dieser Fälle gilt.
     *
     * @param command Eine {@link UpdateCategory}-Instanz, welche validiert werden soll.
     */
    public void validate(UpdateCategory command) {
        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateConstraints(command));
        errors.addAll(checkIfSubcategoriesContainNull(command.getSubCategories()));
        throwIfNeeded(errors);

        throwIfCategoryDoesNotExist(command.getId());

        final List<Category> allCategories = categoryQueryRepository.getPage();
        errors.addAll(checkIfLabelTaken(command.getLabel(), allCategories));
        errors.addAll(checkIfSubcategoryExists(command.getSubCategories(), allCategories));
        errors.addAll(checkIfCycleInSubCategoriesFound(allCategories, command.getSubCategories(),
                new ArrayList<>(Collections.singletonList(command.getId()))));
        throwIfNeeded(errors);
    }

    /**
     * Diese Funktion validiert das Löschen einer Kategorie.
     * <p>
     * In diesem Rahmen wird geprüft, ob ein Constraint verletzt wurden (ein Attribut ist leer oder null),
     * ob die zu löschende Kategorie nicht existiert,
     * und ob die Kategorie ggf. benutzt wird bzw. Karten enthält.
     * Es wird eine {@link de.fantjastisch.cards_backend.util.validation.CommandValidationException} bzw.
     * eine {@link ResponseStatusException} geworfen, sofern einer dieser Fälle gilt.
     *
     * @param command Eine {@link DeleteCategory}-Instanz, welche validiert werden soll.
     */
    public void validate(DeleteCategory command) {
        throwIfNeeded(validateConstraints(command));
        throwIfCategoryDoesNotExist(command.getId());

        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(checkIfCategoryIsInUse(command.getId()));
        throwIfNeeded(errors);
    }

    /**
     * Diese Funktion validiert das Lesen einer Kategorie.
     * <p>
     * In diesem Rahmen wird geprüft, ob die zu Kategorie nicht existiert.
     * Es wird eine {@link ResponseStatusException} geworfen, sofern dieser Fall eintritt.
     *
     * @param categoryId Die ID der Kategorie, welche gelöscht werden soll.
     */
    public void validateGet(UUID categoryId) {
        throwIfCategoryDoesNotExist(categoryId);
    }

    private void throwIfCategoryDoesNotExist(final UUID categoryId) {
        Category category = categoryQueryRepository.get(categoryId);
        if (category == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found"
            );
        }
    }

    private List<ErrorEntry> checkIfCategoryIsInUse(final UUID categoryId) {
        if (cardQueryRepository.isCategoryEmpty(categoryId)) {
            return Collections.emptyList();
        }
        return Collections.singletonList(ErrorEntry.builder()
                .code(CATEGORY_NOT_EMPTY_VIOLATION)
                .field("id")
                .build()
        );
    }

    private List<ErrorEntry> checkIfSubcategoryExists(List<UUID> subCategories, List<Category> allCategories) {
        for (UUID subCategoryId : subCategories) {

            if (allCategories.stream().noneMatch(category -> category.getId().equals(subCategoryId))) {
                return Collections.singletonList(
                        ErrorEntry.builder()
                                .code(SUBCATEGORY_DOESNT_EXIST_VIOLATION)
                                .field("subCategories")
                                .build());
            }
        }
        return Collections.emptyList();
    }

    private List<ErrorEntry> checkIfSubcategoriesContainNull(List<UUID> uuids) {
        List<ErrorEntry> errors = new ArrayList<>();
        if (uuids.contains(null)) {
            errors.add(
                    ErrorEntry.builder()
                            .code(SUBCATEGORY_IS_NULL_VIOLATION)
                            .field("subCategories")
                            .build());

        }
        return errors;
    }

    private List<ErrorEntry> checkIfCycleInSubCategoriesFound
            (List<Category> allCategories, List<UUID> subCategories, ArrayList<UUID> visited) {
        /*
        Prüfe rekursiv, ob beim Aktualisieren der Unterkategorien einer
        vorhandenen Kategorie Zyklen in der Kategorien-Hierarchie entstehen.

        Dafür werden alle Kategorien als Parameter übergeben, sowie die Unterkategorien der zu
        aktualisierenden Kategorie (als Laufparameter), wie auch eine Liste besuchter Kategorien, um den Kreis in der
        Hierarchie feststellen zu können.
         */

        List<ErrorEntry> errors = new ArrayList<>();
        // Prüfe für alle Unterkategorien der zu aktualisierenden Kategorie:
        for (UUID subCategory : subCategories) {
            // Wurde diese Kategorie bereits traversiert, so ist ein Kreis in der Struktur entstanden. Fehler!
            if (visited.contains(subCategory)) {
                errors.add(
                        ErrorEntry.builder()
                                .code(CYCLIC_SUBCATEGORY_RELATION_VIOLATION)
                                .field("subCategories")
                                .build());
                break;
            }
            // Hole die Unterkategorie über die UUID ein
            Category categoryFromUUID = allCategories
                    .stream()
                    .filter(category -> category.getId().equals(subCategory))
                    .toList()
                    .get(0);

            // Betrachte ihre Unterkategorien rekursiv und vermerke aktuelle Kategorie als besucht
            ArrayList<UUID> subSubCategories = new ArrayList<>(categoryFromUUID.getSubCategories());
            visited.add(subCategory);
            errors.addAll(checkIfCycleInSubCategoriesFound(allCategories, subSubCategories, visited));
        }
        return errors;
    }

    private List<ErrorEntry> checkIfLabelTaken(String label, List<Category> allCategories) {
        List<ErrorEntry> errors = new ArrayList<>();
        List<Category> withSameName = allCategories.stream()
                .filter(category -> category.getLabel().equals(label))
                .toList();
        if (!withSameName.isEmpty()) {
            errors.add(
                    ErrorEntry.builder()
                            .code(LABEL_TAKEN_VIOLATION)
                            .field("label")
                            .build());
        }
        return errors;
    }
}
