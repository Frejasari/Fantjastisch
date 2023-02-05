package de.fantjastisch.cards_backend.category.aggregate;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.category.validator.CategoryValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Das CategoryAggregate stellt die Verbindung zwischen dem Controller und dem Persistance-Layer her, fungiert also
 * als Command-Handler für CRUD-Kommando-Objekte, welcher die eingehenden Kommandos vorher mit dem {@link CategoryValidator} validiert.
 *
 * @author Semjon Nirmann
 */
@Component
public class CategoryAggregate {
    private final CategoryCommandRepository categoryCommandRepository;
    private final CategoryValidator categoryValidator;
    private final CategoryQueryRepository categoryQueryRepository;
    private final UUIDGenerator uuidGenerator;

    @Autowired
    public CategoryAggregate(
            CategoryCommandRepository categoryCommandRepository,
            CategoryValidator categoryValidator,
            CategoryQueryRepository categoryQueryRepository,
            UUIDGenerator uuidGenerator) {
        this.categoryCommandRepository = categoryCommandRepository;
        this.categoryQueryRepository = categoryQueryRepository;
        this.categoryValidator = categoryValidator;
        this.uuidGenerator = uuidGenerator;
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Erstellen einer Kategorie.
     *
     * @param command Das CRUD-Kommando-Objekt zum Erstellen einer Kategorie.
     * @return Eine UUID, die die erstellte Entität darstellt.
     */
    public UUID handle(final CreateCategory command) {
        categoryValidator.validate(command);

        final Set<UUID> subCategories = new HashSet<>(command.getSubCategories());
        subCategories.remove(null);
        Category category = Category.builder()
                .id(uuidGenerator.randomUUID())
                .label(command.getLabel().trim())
                .subCategories(subCategories)
                .build();
        categoryCommandRepository.create(category);

        return category.getId();
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Aktualisieren einer Kategorie.
     *
     * @param command Das CRUD-Kommando-Objekt zum Aktualisieren einer Kategorie.
     */
    public void handle(final UpdateCategory command) {
        categoryValidator.validate(command);

        final Set<UUID> subCategories = new HashSet<>(command.getSubCategories());
        subCategories.remove(null);
        final Category updatedCategory = Category.builder()
                .id(command.getId())
                .label(command.getLabel().trim())
                .subCategories(subCategories)
                .build();

        categoryCommandRepository.update(updatedCategory);
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Löschen einer Kategorie.
     *
     * @param categoryId Die UUID einer zu löschenden Kategorie.
     */
    public void handleDelete(final UUID categoryId) {
        categoryValidator.validateDelete(categoryId);
        categoryCommandRepository.delete(categoryId);
    }

    /**
     * Diese Funktion validiert und vermittelt eine Anfrage zum Lesen einer Kategorie.
     *
     * @param categoryId Die UUID einer Kategorie, welche angefordert wird.
     * @return Die entsprechende Entität der Kategorie, gekapselt in einer {@link Category}-Instanz.
     */
    public Category handle(final UUID categoryId) {
        categoryValidator.validateGet(categoryId);
        return categoryQueryRepository.get(categoryId);
    }

    /**
     * Diese Funktion liest alle Kategorien als Liste aus.
     *
     * @return Eine Liste aller Entitäten vom Typ Kategorie, gekapselt in entsprechenden {@link Category}-Instanzen.
     */
    public List<Category> handle() {
        return categoryQueryRepository.getPage();
    }
}
