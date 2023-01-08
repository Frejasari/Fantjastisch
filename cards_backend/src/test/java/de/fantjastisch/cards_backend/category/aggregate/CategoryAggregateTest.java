package de.fantjastisch.cards_backend.category.aggregate;

import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.category.validator.CategoryValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class CategoryAggregateTest {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private CategoryAggregate categoryAggregate;

    @Mock
    private UUIDGenerator uuidGenerator;

    @BeforeEach
    public void setUp() {
        CategoryCommandRepository categoryCommandRepository = new CategoryCommandRepository(namedParameterJdbcTemplate);
        CategoryQueryRepository categoryQueryRepository = new CategoryQueryRepository(namedParameterJdbcTemplate);
        CategoryValidator categoryValidator = new CategoryValidator();
        categoryAggregate = new CategoryAggregate(categoryCommandRepository, categoryValidator, categoryQueryRepository, uuidGenerator);
    }

    @Test
    public void shouldThrowWhenLabelTaken() {
        final UUID categoryId = UUID.fromString("d8838186-09b6-44a5-b386-35c6c3bc8e5e");
        when(uuidGenerator.randomUUID())
                .thenReturn(
                        categoryId
                );

        CreateCategory category = CreateCategory.builder()
                .label("Mathematik")
                .subCategories(Collections.emptyList())
                .build();
        // insert
        categoryAggregate.handle(category);

        CreateCategory toCreate = CreateCategory.builder()
                .label("Mathematik")
                .subCategories(Collections.emptyList())
                .build();

        ErrorEntry labelTakenError = ErrorEntry.builder()
                .code(LABEL_TAKEN)
                .field("label")
                .build();
        CommandValidationException exception = assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(toCreate));
        assertTrue(exception.getErrors().contains(labelTakenError));


        UpdateCategory toUpdate = UpdateCategory.builder()
                .id(UUID.fromString("b1f5e79c-2f2b-4a48-bf93-5a2439f2301e"))
                .label("Praktische Informatik").subCategories(Collections.emptyList()).build();

        exception = Assertions.assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(toUpdate));

        assertTrue(exception.getErrors().contains(labelTakenError));
    }

    @Test
    public void shouldThrowWhenCategoryNotFound() {
        UpdateCategory toUpdate = UpdateCategory.builder().id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725")).label("NOT FOUND").subCategories(Collections.emptyList()).build();
        assertThrows(ResponseStatusException.class, () -> categoryAggregate.handle(toUpdate));

        DeleteCategory toDelete = DeleteCategory.builder().id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725")).build();
        assertThrows(ResponseStatusException.class, () -> categoryAggregate.handle(toDelete));
    }

    @Test
    public void shouldThrowWhenBlankField() {
        CreateCategory toCreate = CreateCategory.builder().label("").subCategories(Collections.emptyList()).build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(toCreate));
        ErrorEntry blankLabel = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("label")
                .build();
        assertTrue(exception.getErrors().contains(blankLabel));

        UpdateCategory toUpdate = UpdateCategory.builder().id(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323")).label("").subCategories(Collections.emptyList()).build();

        exception = Assertions.assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(toUpdate));

        assertTrue(exception.getErrors().contains(blankLabel));
    }

    @Test
    public void shouldThrowWhenNull() {
        CreateCategory toCreate = CreateCategory.builder().label(null).subCategories(Collections.emptyList()).build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(toCreate));
        ErrorEntry blankLabel = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("label")
                .build();
        assertTrue(exception.getErrors().contains(blankLabel));
    }

    @Test
    public void shouldThrowWhenCycle() {
        CreateCategory c = CreateCategory.builder().label("c").subCategories(Collections.emptyList()).build();
        when(uuidGenerator.randomUUID())
                .thenReturn(
                        UUID.fromString("9db2d0a7-6733-4678-9c1d-4defbe9b425f"),
                        UUID.fromString("1ed3f052-bbe3-4e75-a26f-73e1705b8f1b"),
                        UUID.fromString("0d090c42-99b0-46a0-8bec-112936437cad")
                );
        UUID idOfC = categoryAggregate.handle(c);
        CreateCategory b = CreateCategory.builder().label("b").subCategories(Collections.singletonList(idOfC)).build();
        UUID idOfB = categoryAggregate.handle(b);
        CreateCategory a = CreateCategory.builder().label("a").subCategories(Collections.singletonList(idOfB)).build();
        UUID idOfA = categoryAggregate.handle(a);

        UpdateCategory newC = UpdateCategory.builder().id(idOfC).label("c").subCategories(Collections.singletonList(idOfA)).build();
        CommandValidationException exception = assertThrows(CommandValidationException.class, () -> categoryAggregate.handle(newC));
        ErrorEntry cyclicSubcategoryError = ErrorEntry.builder()
                .code(CYCLIC_SUBCATEGORY_RELATION)
                .field("subCategories")
                .build();
        assertTrue(exception.getErrors().contains(cyclicSubcategoryError));

    }
    // cant add self
    // cant add parent to subcategories ...
}
