package de.fantjastisch.cards_backend.category;

import de.fantjastisch.cards_backend.category.aggregate.CategoryAggregate;
import de.fantjastisch.cards_backend.category.aggregate.CreateCategory;
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

import java.util.*;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


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

    private final Category category = Category
            .builder()
            .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
            .label("Mathematik")
            .subCategories(Collections.emptySet())
            .build();

    @BeforeEach
    public void setUp() {
        CategoryValidator categoryValidator = new CategoryValidator(categoryQueryRepository);
        categoryAggregate = new CategoryAggregate(categoryCommandRepository, categoryValidator, categoryQueryRepository, uuidGenerator);
    }

    @Test
    public void shouldCreate() {
        when(categoryQueryRepository.getPage()).thenReturn(Collections.emptyList());
        when(uuidGenerator.randomUUID()).thenReturn(category.getId());

        CreateCategory toCreate = CreateCategory.builder()
                .label(category.getLabel())
                .subCategories(category.getSubCategories())
                .build();

        categoryAggregate.handle(toCreate);
        verify(categoryCommandRepository, times(1)).create(category);
    }

    @Test
    public void shouldCreateOnNullSubcategorie() {
        when(categoryQueryRepository.getPage()).thenReturn(Collections.emptyList());
        when(uuidGenerator.randomUUID()).thenReturn(category.getId());

        Set<UUID> subCategories = new HashSet<>();
        subCategories.add(null);
        CreateCategory toCreate = CreateCategory.builder()
                .label(category.getLabel())
                .subCategories(subCategories)
                .build();

        categoryAggregate.handle(toCreate);
        verify(categoryCommandRepository, times(1)).create(category);
    }


    @Test
    public void shouldUpdate() {
        when(categoryQueryRepository.getPage()).thenReturn(Collections.singletonList(category));
        when(categoryQueryRepository.get(category.getId())).thenReturn(category);

        UpdateCategory toUpdate = UpdateCategory.builder()
                .id(category.getId())
                .label("new Label")
                .subCategories(Collections.emptySet())
                .build();

        categoryAggregate.handle(toUpdate);
        verify(categoryCommandRepository, times(1)).update(category
                .toBuilder()
                .label(toUpdate.getLabel())
                .build());
    }

    @Test
    public void shouldDelete() {
        when(categoryQueryRepository.get(category.getId())).thenReturn(category);
        when(categoryQueryRepository.isCategoryEmpty(category.getId())).thenReturn(true);

        categoryAggregate.handleDelete(category.getId());
        verify(categoryCommandRepository, times(1)).delete(category.getId());
    }

    @Test
    public void shouldGet() {
        when(categoryQueryRepository.get(category.getId())).thenReturn(category);

        categoryAggregate.handle(category.getId());
        verify(categoryQueryRepository, times(2)).get(category.getId());
    }

    @Test
    public void shouldGetPage() {
        categoryAggregate.handle();
        verify(categoryQueryRepository, times(1)).getPage();
    }

    @Test
    public void shouldThrowWhenLabelTaken() {
        when(categoryQueryRepository.getPage()).thenReturn(Collections.singletonList(category));

        CreateCategory toCreate = CreateCategory.builder()
                .label(category.getLabel())
                .subCategories(Collections.emptySet())
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
        when(categoryQueryRepository.isCategoryEmpty(category.getId())).thenReturn(false);
        when(categoryQueryRepository.get(category.getId())).thenReturn(category);
        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> categoryAggregate.handleDelete(category.getId()));
        assertTrue(exception.getErrors().contains(ErrorEntry
                .builder()
                .code(CATEGORY_NOT_EMPTY_VIOLATION)
                .field("id").build()
        ));
    }

    @Test
    public void shouldThrowWhenSubCategoryDoesNotExist() {
        when(categoryQueryRepository.getPage()).thenReturn(Collections.emptyList());

        CreateCategory toCreate = CreateCategory.builder()
                .label(category.getLabel())
                .subCategories(Set.of(UUID.fromString("a8f44f64-ba23-4a3b-b847-928085005eed")))
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> categoryAggregate.handle(toCreate));

        assertTrue(exception.getErrors().contains(ErrorEntry
                .builder()
                .code(SUBCATEGORY_DOESNT_EXIST_VIOLATION)
                .field("subCategories")
                .build()
        ));
    }

    @Test
    public void shouldThrowWhenCategoryNotFound() {
        UpdateCategory toUpdate = UpdateCategory.builder()
                .id(category.getId())
                .label("NOT FOUND").subCategories(Collections.emptySet())
                .build();
        assertThrows(EntityDoesNotExistException.class, () -> categoryAggregate.handle(toUpdate));

        UUID toDelete = UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725");
        assertThrows(EntityDoesNotExistException.class, () -> categoryAggregate.handle(toDelete));
    }

    @Test
    public void shouldThrowWhenBlankField() {
        CreateCategory toCreate = CreateCategory
                .builder()
                .label("")
                .subCategories(Collections.emptySet())
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
                .subCategories(Collections.emptySet())
                .build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class,
                () -> categoryAggregate.handle(toCreate));
        ErrorEntry blankLabel = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("label")
                .build();
        assertTrue(exception.getErrors().contains(blankLabel));
    }

    @Test
    public void shouldThrowWhenCycle() {
        final UUID idOfC = UUID.fromString("9db2d0a7-6733-4678-9c1d-4defbe9b425f");
        final UUID idOfB = UUID.fromString("1ed3f052-bbe3-4e75-a26f-73e1705b8f1b");
        final UUID idOfA = UUID.fromString("0d090c42-99b0-46a0-8bec-112936437cad");

        final Category catC = Category
                .builder()
                .id(idOfC)
                .label("c")
                .subCategories(Collections.emptySet())
                .build();
        final Category catB = Category
                .builder()
                .id(idOfB)
                .label("b")
                .subCategories(Set.of(idOfC))
                .build();
        final Category catA = Category
                .builder()
                .id(idOfA)
                .label("a")
                .subCategories(Set.of(idOfB))
                .build();
        final UpdateCategory newC = UpdateCategory
                .builder()
                .id(idOfC)
                .label("c")
                .subCategories(Set.of(idOfA))
                .build();

        when(categoryQueryRepository.getPage()).thenReturn(Arrays.asList(catC, catB, catA));
        when(categoryQueryRepository.get(newC.getId())).thenReturn(catC);
        CommandValidationException exception = assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(newC));
        ErrorEntry cyclicSubcategoryError = ErrorEntry.builder()
                .code(CYCLIC_SUBCATEGORY_RELATION_VIOLATION)
                .field("subCategories")
                .build();
        assertTrue(exception.getErrors().contains(cyclicSubcategoryError));
    }

    @Test
    public void shouldThrowWhenUpdateLabelExists() {
        final UUID idOfC = UUID.fromString("6db2d0a7-6733-4678-9c1d-4defbe9b425f");
        final UUID idOfA = UUID.fromString("8db2d0a7-6733-4678-9c1d-4defbe9b425f");
        final Category catC = Category
                .builder()
                .id(idOfC)
                .label(category.getLabel())
                .subCategories(category.getSubCategories())
                .build();
        final Category catA = Category
                .builder()
                .id(idOfA)
                .label("egal")
                .subCategories(category.getSubCategories())
                .build();
        categoryCommandRepository.create(catC);
        categoryCommandRepository.create(catA);

        when(categoryQueryRepository.get(catA.getId())).thenReturn(catA);

        when(categoryQueryRepository.getPage()).thenReturn(List.of(catC, catA));

        UpdateCategory toUpdate = UpdateCategory.builder()
                .id(catA.getId())
                .label(catC.getLabel())
                .subCategories(catA.getSubCategories())
                .build();
        CommandValidationException exception = assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(toUpdate));
        ErrorEntry nullSubcategoryError = ErrorEntry.builder()
                .code(LABEL_TAKEN_VIOLATION)
                .field("label")
                .build();
        assertTrue(exception.getErrors().contains(nullSubcategoryError));

    }
}
