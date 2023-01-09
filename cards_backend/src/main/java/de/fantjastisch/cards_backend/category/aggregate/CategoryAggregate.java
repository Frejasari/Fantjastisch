package de.fantjastisch.cards_backend.category.aggregate;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.category.validator.CategoryValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        categoryValidator.validate(command);

        Category category = Category.builder()
                .id(uuidGenerator.randomUUID())
                .label(command.getLabel())
                .subCategories(command.getSubCategories())
                .build();
        categoryCommandRepository.create(category);
        return category.getId();
    }

    public void handle(final UpdateCategory command) {
        categoryValidator.validate(command);
        categoryQueryRepository.get(command.getId());
        final Category updatedCategory = Category.builder()
                .id(command.getId())
                .label(command.getLabel())
                .subCategories(command.getSubCategories())
                .build();

        categoryCommandRepository.update(updatedCategory);
    }

    public void handle(final DeleteCategory command) {
        categoryValidator.validate(command);
        Category category = categoryQueryRepository.get(command.getId());
        categoryCommandRepository.delete(category);
    }

    public Category handle(final UUID categoryId) {
        categoryValidator.validateExists(categoryId);
        return categoryQueryRepository.get(categoryId);
    }

    public List<Category> handle() {
        return categoryQueryRepository.getList();
    }

}
