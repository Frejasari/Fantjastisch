package de.fantjastisch.cards_backend.category.aggregate;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.category.validator.CategoryValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    public UUID handle(final CreateCategory command) {
        List<Category> allCategories = categoryQueryRepository.getList();
        if (command.getSubCategories() == null) {
            command.setSubCategories(Collections.emptyList());
        }
        categoryValidator.validate(command, allCategories);

        Category category = Category.builder()
                .id(uuidGenerator.randomUUID())
                .label(command.getLabel())
                .subCategories(command.getSubCategories())
                .build();
        categoryCommandRepository.create(category);
        return category.getId();
    }

    public void handle(final UpdateCategory command) {
        List<Category> allCategories = categoryQueryRepository.getList();
        if (command.getSubCategories() == null) {
            command.setSubCategories(Collections.emptyList());
        }
        
        categoryValidator.validate(command, allCategories);
        throwOrGet(command.getId());
        final Category updatedCategory = Category.builder()
                .id(command.getId())
                .label(command.getLabel())
                .subCategories(command.getSubCategories())
                .build();

        categoryCommandRepository.update(updatedCategory);
    }

    public void handle(final DeleteCategory command) {
        Category category = throwOrGet(command.getId());
        categoryCommandRepository.delete(category);
    }

    public Category handle(UUID id) {
        return throwOrGet(id);
    }

    public List<Category> handle() {
        return categoryQueryRepository.getList();
    }

    private Category throwOrGet(UUID id) {
        Category category = categoryQueryRepository.get(id);
        if (category == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found"
            );
        }
        return category;
    }
}
