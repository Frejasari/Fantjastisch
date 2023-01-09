package de.fantjastisch.cards_backend.category.validator;

import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.aggregate.CreateCategory;
import de.fantjastisch.cards_backend.category.aggregate.DeleteCategory;
import de.fantjastisch.cards_backend.category.aggregate.UpdateCategory;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.util.validation.Validator;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;

@Component
public class CategoryValidator extends Validator {

    private final CardQueryRepository cardQueryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    @Autowired
    public CategoryValidator(CardQueryRepository cardQueryRepository, CategoryQueryRepository categoryQueryRepository) {
        this.cardQueryRepository = cardQueryRepository;
        this.categoryQueryRepository = categoryQueryRepository;
    }

    public void validate(CreateCategory command) {
        throwIfNeeded(validateConstraints(command));

        List<ErrorEntry> errors = new ArrayList<>();
        final List<Category> allCategories = categoryQueryRepository.getList();
        errors.addAll(checkIfLabelTaken(command.getLabel(), allCategories));
        errors.addAll(checkIfSubcategoryExists(command.getSubCategories(), allCategories));
        throwIfNeeded(errors);
    }

    public void validate(UpdateCategory command) {
        throwIfNeeded(validateConstraints(command));
        throwIfCategoryDoesNotExist(command.getId());

        List<ErrorEntry> errors = new ArrayList<>();
        final List<Category> allCategories = categoryQueryRepository.getList();

        errors.addAll(checkIfLabelTaken(command.getLabel(), allCategories));
        errors.addAll(checkIfSubcategoryExists(command.getSubCategories(), allCategories));
        errors.addAll(checkIfCycleInSubCategoriesFound(allCategories, command.getSubCategories(),
                new ArrayList<>(Collections.singletonList(command.getId()))));
        throwIfNeeded(errors);
    }

    public void validate(DeleteCategory command) {
        throwIfNeeded(validateConstraints(command));
        throwIfCategoryDoesNotExist(command.getId());

        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(checkIfCategoryIsInUse(command.getId()));
        throwIfNeeded(errors);
    }


    public void validateExists(UUID categoryId) {
        throwIfCategoryDoesNotExist(categoryId);
    }

    private void throwIfCategoryDoesNotExist(@NotNull final UUID categoryId) {
        Category category = categoryQueryRepository.get(categoryId);
        if (category == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found"
            );
        }
    }

    private List<ErrorEntry> checkIfCategoryIsInUse(@NotNull final UUID categoryId) {
        if (cardQueryRepository.isCategoryEmpty(categoryId)) {
            return Collections.emptyList();
        }
        return Collections.singletonList(ErrorEntry.builder()
                .code(CATEGORY_NOT_EMPTY)
                .field("id")
                .build()
        );
    }

    private List<ErrorEntry> checkIfSubcategoryExists(List<UUID> subCategories, List<Category> allCategories) {
        for (UUID subCategoryId : subCategories) {

            if (allCategories.stream().noneMatch(category -> category.getId().equals(subCategoryId))) {
                return Collections.singletonList(
                        ErrorEntry.builder()
                                .code(SUBCATEGORY_DOESNT_EXIST)
                                .field("subCategories")
                                .build());
            }
        }
        return Collections.emptyList();
    }

    private List<ErrorEntry> checkIfCycleInSubCategoriesFound
            (List<Category> allCategories, List<UUID> subCategories, ArrayList<UUID> visited) {
        // Rekursiv subcategories von einzusetzender Subcategory durchsuchen, wenn alle leer - alles super.
        // Liste mitschleifen von bereits besuchten Subcategories; wenn eine besucht wird - throw.
        List<ErrorEntry> errors = new ArrayList<>();

        for (UUID subCategory : subCategories) {
            if (visited.contains(subCategory)) {
                errors.add(
                        ErrorEntry.builder()
                                .code(CYCLIC_SUBCATEGORY_RELATION)
                                .field("subCategories")
                                .build());
                break;
            }
            Category categoryFromUUID = allCategories
                    .stream()
                    .filter(x -> x.getId().equals(subCategory))
                    .toList()
                    .get(0);

            ArrayList<UUID> subSubCategories = new ArrayList<>(categoryFromUUID.getSubCategories());
            visited.add(subCategory);
            errors.addAll(checkIfCycleInSubCategoriesFound(allCategories, subSubCategories, visited));
        }
        return errors;
    }

    private List<ErrorEntry> checkIfLabelTaken(String label, List<Category> allCategories) {
        List<ErrorEntry> errors = new ArrayList<>();
        List<Category> withSameName = allCategories.stream()
                .filter(x -> x.getLabel().equals(label))
                .toList();
        if (!withSameName.isEmpty()) {
            errors.add(
                    ErrorEntry.builder()
                            .code(LABEL_TAKEN)
                            .field("label")
                            .build());
        }
        return errors;
    }

}
