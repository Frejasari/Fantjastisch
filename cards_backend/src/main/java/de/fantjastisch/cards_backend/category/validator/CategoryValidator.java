package de.fantjastisch.cards_backend.category.validator;

import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.aggregate.CreateCategory;
import de.fantjastisch.cards_backend.category.aggregate.UpdateCategory;
import de.fantjastisch.cards_backend.util.validation.Validator;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;

@Component
@NoArgsConstructor
public class CategoryValidator extends Validator {

    public void validate(CreateCategory command, List<Category> allCategories) {
        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateConstraints(command));
        errors.addAll(checkIfLabelTaken(command.getLabel(), allCategories));
        errors.addAll(checkIfSubcategoryExists(command.getSubCategories(), allCategories));
        throwIfNeeded(errors);
    }

    public void validate(UpdateCategory command, List<Category> allCategories) {
        List<ErrorEntry> errors = new ArrayList<>();
        errors.addAll(validateConstraints(command));
        errors.addAll(checkIfLabelTaken(command.getLabel(), allCategories));
        errors.addAll(checkIfSubcategoryExists(command.getSubCategories(), allCategories));
        errors.addAll(checkIfCycleInSubCategoriesFound(allCategories, command.getSubCategories(), new ArrayList<UUID>(Arrays.asList(command.getId()))));
        throwIfNeeded(errors);
    }

    private List<ErrorEntry> checkIfSubcategoryExists(List<UUID> subCategories, List<Category> allCategories) {
        List<ErrorEntry> errors = new ArrayList<>();
        for (UUID subCategoryId : subCategories) {
            if (allCategories.stream().filter(x -> x.getId().equals(subCategoryId)).collect(Collectors.toList()).isEmpty()) {
                errors.add(
                        ErrorEntry.builder()
                                .code(SUBCATEGORY_DOESNT_EXIST)
                                .field("subCategories")
                                .build());
            }
        }
        return errors;
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
                .collect(Collectors.toList());
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
