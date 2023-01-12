package de.fantjastisch.cards_backend.card;

import de.fantjastisch.cards_backend.card.aggregate.CardAggregate;
import de.fantjastisch.cards_backend.card.aggregate.CreateCard;
import de.fantjastisch.cards_backend.card.aggregate.DeleteCard;
import de.fantjastisch.cards_backend.card.aggregate.UpdateCard;
import de.fantjastisch.cards_backend.card.repository.CardCommandRepository;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.card.validator.CardValidator;
import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.category.repository.CategoryCommandRepository;
import de.fantjastisch.cards_backend.category.repository.CategoryQueryRepository;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardAggregateTests {
    private CardAggregate cardAggregate;

    @Mock
    private UUIDGenerator uuidGenerator;
    @Mock
    private CardCommandRepository cardCommandRepository;
    @Mock
    private CardQueryRepository cardQueryRepository;

    @Mock
    private CategoryCommandRepository categoryCommandRepository;
    @Mock
    private CategoryQueryRepository categoryQueryRepository;

    private final Category category = Category.builder()
            .id(UUID.randomUUID())
            .label("SWP")
            .subCategories(Collections.emptyList())
            .build();
    private final Card card = Card
            .builder()
            .id(UUID.fromString("b7913a6f-6152-436e-b3ef-e38eb54d4725"))
            .question("I am the question")
            .answer("I am the answer")
            .tag("I am a tag")
            .categories(List.of(category.getId()))
            .build();


    @BeforeEach
    public void setUp() {
        CardValidator cardValidator = new CardValidator(cardQueryRepository, categoryQueryRepository);
        cardAggregate = new CardAggregate(cardCommandRepository, cardQueryRepository, cardValidator, uuidGenerator);
        categoryCommandRepository.create(category);
        cardCommandRepository.create(card);
    }

    @Test
    public void shouldThrowWhenQuestionBlank() {
        CreateCard toCreate = CreateCard.builder()
                .question("")
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(card.getCategories())
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toCreate));

        ErrorEntry blankError = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("question")
                .build();
        assertTrue(exception.getErrors().contains(blankError));


        UpdateCard toUpdate = UpdateCard.builder()
                .id(card.getId())
                .question("")
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(card.getCategories())
                .build();

        exception = assertThrows(CommandValidationException.class, () -> cardAggregate.handle(toUpdate));

        assertTrue(exception.getErrors().contains(blankError));
    }

    @Test
    public void shouldThrowWhenAnswerBlank() {
        CreateCard toCreate = CreateCard.builder()
                .question(card.getQuestion())
                .answer("")
                .tag(card.getTag())
                .categories(card.getCategories())
                .build();

        UpdateCard toUpdate = UpdateCard.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer("")
                .tag(card.getTag())
                .categories(card.getCategories())
                .build();

        ErrorEntry blankError = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("answer")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toCreate));
        CommandValidationException exceptionUpdate = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toUpdate));

        assertTrue(exception.getErrors().contains(blankError));
        assertTrue(exceptionUpdate.getErrors().contains(blankError));

    }

    @Test
    public void shouldThrowWhenTagBlank() {
        CreateCard toCreate = CreateCard.builder()
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag("")
                .categories(card.getCategories())
                .build();

        UpdateCard toUpdate = UpdateCard.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag("")
                .categories(card.getCategories())
                .build();

        ErrorEntry blankError = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("tag")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toCreate));
        CommandValidationException exceptionUpdate = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toUpdate));

        assertTrue(exception.getErrors().contains(blankError));
        assertTrue(exceptionUpdate.getErrors().contains(blankError));

    }

    @Test
    public void shouldThrowWhenCategoriesEmptyOrNull() {
        CreateCard toCreate = CreateCard.builder()
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(List.of())
                .build();

        UpdateCard toUpdate = UpdateCard.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(List.of())
                .build();

        ErrorEntry constraintError = ErrorEntry.builder()
                .code(CONSTRAINT_VIOLATION)
                .field("categories")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toCreate));
        CommandValidationException exceptionUpdate = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toUpdate));
        assertTrue(exception.getErrors().contains(constraintError));
        assertTrue(exceptionUpdate.getErrors().contains(constraintError));

        CreateCard toCreateNull = CreateCard.builder()
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(null)
                .build();

        exception = assertThrows(CommandValidationException.class, () -> cardAggregate.handle(toCreateNull));
        assertTrue(exception.getErrors().contains(constraintError));

        UpdateCard toUpdateNull = UpdateCard.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(null)
                .build();

        exception = assertThrows(CommandValidationException.class, () -> cardAggregate.handle(toUpdateNull));
        assertTrue(exception.getErrors().contains(constraintError));
    }

    @Test
    public void shouldThrowWhenCardNonExistent() {

        DeleteCard toDelete = DeleteCard.builder().id(UUID.randomUUID()).build();
        UpdateCard toUpdate = UpdateCard.builder()
                .id(UUID.randomUUID())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(Collections.singletonList(category.getId()))
                .build();

        assertThrows(ResponseStatusException.class, () -> cardAggregate.handle(toDelete));
        assertThrows(ResponseStatusException.class, () -> cardAggregate.handle(toUpdate));
    }

    @Test
    public void shouldThrowWhenCategoriesNonExistent() {
        CreateCard toCreate = CreateCard.builder()
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(List.of(UUID.randomUUID()))
                .build();

        ErrorEntry categoryError = ErrorEntry.builder()
                .code(CATEGORY_DOESNT_EXIST_VIOLATION)
                .field("categories")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toCreate));
        assertTrue(exception.getErrors().contains(categoryError));

        UpdateCard toUpdate = UpdateCard.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(List.of(UUID.randomUUID()))
                .build();

        ErrorEntry categoryError1 = ErrorEntry.builder()
                .code(CATEGORY_DOESNT_EXIST_VIOLATION)
                .field("categories")
                .build();

        when(cardQueryRepository.get(card.getId())).thenReturn(card);

        exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toUpdate));
        assertTrue(exception.getErrors().contains(categoryError1));
    }

    @Test
    public void shouldThrowWhenDuplicateCard() {
        when(cardQueryRepository.getPage(null, null, null, false)).thenReturn(Collections.singletonList(card));
        when(categoryQueryRepository.get(category.getId())).thenReturn(category);

        CreateCard toCreate = CreateCard.builder()
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(card.getCategories())
                .build();

        ErrorEntry duplicateError = ErrorEntry.builder()
                .code(CARD_DUPLICATE)
                .field("question")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toCreate));
        System.out.print(exception.getErrors());
        assertTrue(exception.getErrors().contains(duplicateError));
    }

    @Test
    public void shouldThrowNullQuestion() {
        CreateCard toCreate = CreateCard.builder()
                .question(null)
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(card.getCategories())
                .build();

        ErrorEntry constraintError = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("question")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toCreate));
        System.out.print(exception.getErrors());
        assertTrue(exception.getErrors().contains(constraintError));

        UpdateCard toUpdate = UpdateCard.builder()
                .id(card.getId())
                .question(null)
                .answer(card.getAnswer())
                .tag(card.getTag())
                .categories(List.of())
                .build();

        exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toUpdate));
        assertTrue(exception.getErrors().contains(constraintError));
    }

    @Test
    public void shouldThrowNullAnswer() {
        CreateCard toCreate = CreateCard.builder()
                .question(card.getQuestion())
                .answer(null)
                .tag(card.getTag())
                .categories(card.getCategories())
                .build();

        ErrorEntry constraintError = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("answer")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toCreate));
        System.out.print(exception.getErrors());
        assertTrue(exception.getErrors().contains(constraintError));

        UpdateCard toUpdate = UpdateCard.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(null)
                .tag(card.getTag())
                .categories(List.of())
                .build();

        exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toUpdate));
        assertTrue(exception.getErrors().contains(constraintError));
    }

    @Test
    public void shouldThrowNullTag() {
        CreateCard toCreate = CreateCard.builder()
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(null)
                .categories(card.getCategories())
                .build();

        ErrorEntry constraintError = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("tag")
                .build();

        CommandValidationException exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toCreate));
        System.out.print(exception.getErrors());
        assertTrue(exception.getErrors().contains(constraintError));

        UpdateCard toUpdate = UpdateCard.builder()
                .id(card.getId())
                .question(card.getQuestion())
                .answer(card.getAnswer())
                .tag(null)
                .categories(List.of())
                .build();

        exception = assertThrows(CommandValidationException.class,
                () -> cardAggregate.handle(toUpdate));
        assertTrue(exception.getErrors().contains(constraintError));
    }
}
