package de.fantjastisch.cards_backend.category;

import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.category.aggregate.CategoryAggregate;
import de.fantjastisch.cards_backend.category.aggregate.CreateCategory;
import de.fantjastisch.cards_backend.category.aggregate.DeleteCategory;
import de.fantjastisch.cards_backend.category.aggregate.UpdateCategory;
import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.category.validator.CategoryValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.EntityDoesNotExistException;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


/**
 * Test Klasse für die Category Repositories
 *
 * @author Freja Sender, Semjon Nirmann
 */
@ExtendWith(MockitoExtension.class)
public class CategoryAggregateTest {

    private CategoryAggregate categoryAggregate;

    @Mock
    private UUIDGenerator uuidGenerator;
    @Mock
    private CategoryCommandRepository categoryCommandRepository;
    @Mock
    private CategoryQueryRepository categoryQueryRepository;
    @Mock
    CardQueryRepository cardQueryRepository;

    private final de.fantjastisch.cards_backend.category.repository.Category category = de.fantjastisch.cards_backend.category.repository.Category
            .builder()
            .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
            .label("Mathematik")
            .subCategories(Collections.emptyList())
            .build();

    @BeforeEach
    public void setUp() {
        CategoryValidator categoryValidator = new CategoryValidator(cardQueryRepository, categoryQueryRepository);
        categoryAggregate = new CategoryAggregate(categoryCommandRepository, categoryValidator, categoryQueryRepository, uuidGenerator);
    }

    @Test
    public void jackson(){
        
    }

    @Test
    public void shouldThrowWhenLabelTaken() {
        //when(categoryQueryRepository.getPage()).thenReturn(Collections.singletonList(category));

        CreateCategory toCreate = CreateCategory.builder()
                .label(category.getLabel())
                .subCategories(Collections.emptyList())
                .build();

        ErrorEntry labelTakenError = ErrorEntry.builder()
                .code(LABEL_TAKEN_VIOLATION)
                .field("label")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> categoryAggregate.handle(toCreate));
        assertTrue(exception.getErrors().contains(labelTakenError));
    }


    @Test
    public void shouldThrowWhenCategoryNotEmpty() {
        when(cardQueryRepository.isCategoryEmpty(category.getId())).thenReturn(false);
       // when(categoryQueryRepository.get(category.getId())).thenReturn(category);
        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> categoryAggregate.handle(DeleteCategory.builder().id(category.getId()).build()));
        assertTrue(exception.getErrors().contains(ErrorEntry
                .builder()
                .code(CATEGORY_NOT_EMPTY_VIOLATION)
                .field("id").build()
        ));
    }

    @Test
    public void shouldThrowWhenCategoryNotFound() {
        UpdateCategory toUpdate = UpdateCategory.builder()
                .id(category.getId())
                .label("NOT FOUND").subCategories(Collections.emptyList())
                .build();
        assertThrows(EntityDoesNotExistException.class, () -> categoryAggregate.handle(toUpdate));

        DeleteCategory toDelete = DeleteCategory.builder().id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725")).build();
        assertThrows(EntityDoesNotExistException.class, () -> categoryAggregate.handle(toDelete));
    }

    @Test
    public void shouldThrowWhenBlankField() {
        CreateCategory toCreate = CreateCategory
                .builder()
                .label("")
                .subCategories(Collections.emptyList())
                .build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class,
                () -> categoryAggregate.handle(toCreate));

        ErrorEntry blankLabel = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("label")
                .build();

        assertTrue(exception.getErrors().contains(blankLabel));

        UpdateCategory toUpdate = UpdateCategory
                .builder()
                .id(category.getId())
                .label("")
                .subCategories(category.getSubCategories())
                .build();

        exception = Assertions.assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(toUpdate));

        assertTrue(exception.getErrors().contains(blankLabel));
    }

    @Test
    public void
    shouldThrowWhenSubCategoriesNull() {

        CreateCategory toCreate = CreateCategory.builder()
                .label("new")
                .subCategories(null)
                .build();

        ErrorEntry labelTakenError = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("subCategories")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> categoryAggregate.handle(toCreate));
        assertTrue(exception.getErrors().contains(labelTakenError));
    }

    @Test
    public void shouldThrowWhenLabelNull() {
        CreateCategory toCreate = CreateCategory
                .builder()
                .label(null)
                .subCategories(Collections.emptyList())
                .build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class,
                () -> categoryAggregate.handle(toCreate));
        ErrorEntry blankLabel = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("label")
                .build();
        assertTrue(exception.getErrors().contains(blankLabel));
    }
/*
    @Test
    public void shouldThrowWhenCycle() {
        final UUID idOfC = UUID.fromString("9db2d0a7-6733-4678-9c1d-4defbe9b425f");
        final UUID idOfB = UUID.fromString("1ed3f052-bbe3-4e75-a26f-73e1705b8f1b");
        final UUID idOfA = UUID.fromString("0d090c42-99b0-46a0-8bec-112936437cad");

        final Category catC = Category
                .builder()
                .id(idOfC)
                .label("c")
                .subCategories(Collections.emptyList())
                .build();
        final de.fantjastisch.cards_backend.category.repository.Category catB = Category
                .builder()
                .id(idOfB)
                .label("b")
                .subCategories(Collections.singletonList(idOfC))
                .build();
        final Category catA = Category
                .builder()
                .id(idOfA)
                .label("a")
                .subCategories(Collections.singletonList(idOfB))
                .build();
        final UpdateCategory newC = UpdateCategory
                .builder()
                .id(idOfC)
                .label("c")
                .subCategories(Collections.singletonList(idOfA))
                .build();

        when(categoryQueryRepository.getPage()).thenReturn(Arrays.asList(catC, catB, catA));
        when(categoryQueryRepository.get(newC.getId())).thenReturn(catC);
        CommandValidationException exception = assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(newC));
        ErrorEntry cyclicSubcategoryError = ErrorEntry.builder()
                .code(CYCLIC_SUBCATEGORY_RELATION_VIOLATION)
                .field("subCategories")
                .build();
        assertTrue(exception.getErrors().contains(cyclicSubcategoryError));

    }*/

    @Test
    public void shouldThrowWhenSubcategoryIsNull() {
        // create
        CreateCategory cat = CreateCategory
                .builder()
                .label("cat")
                .subCategories(Collections.singletonList(null))
                .build();
        CommandValidationException exception = assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(cat));
        ErrorEntry nullSubcategoryError = ErrorEntry.builder()
                .code(SUBCATEGORY_IS_NULL_VIOLATION)
                .field("subCategories")
                .build();
        assertTrue(exception.getErrors().contains(nullSubcategoryError));

        // update
        final UUID id = UUID.fromString("9db2d0a7-6733-4678-9c1d-4defbe9b425f");
        final Category newCat = Category
                .builder()
                .id(id)
                .label("cat")
                .subCategories(Collections.emptyList())
                .build();

        UpdateCategory updateNewCat = UpdateCategory.builder()
                .id(id)
                .label("cat")
                .subCategories(Collections.singletonList(null))
                .build();

        CommandValidationException exception2 = assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(updateNewCat));

        assertTrue(exception2.getErrors().contains(nullSubcategoryError));

    }
    // cant add self
    // cant add parent to subcategories ...
}
