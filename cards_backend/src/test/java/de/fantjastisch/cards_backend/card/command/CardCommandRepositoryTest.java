package de.fantjastisch.cards_backend.card.command;

import de.fantjastisch.cards_backend.card.Card;
import de.fantjastisch.cards_backend.card.aggregate.CardAggregate;
import de.fantjastisch.cards_backend.card.aggregate.CreateCard;
import de.fantjastisch.cards_backend.card.repository.CardCommandRepository;
import de.fantjastisch.cards_backend.card.repository.CardQueryRepository;
import de.fantjastisch.cards_backend.card.validator.CardValidator;
import de.fantjastisch.cards_backend.category.Category;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static de.fantjastisch.cards_backend.util.validation.errors.ErrorCode.*;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class CardCommandRepositoryTest {

    private CardCommandRepository cardCommandRepository;
    private CardQueryRepository cardQueryRepository;
    private CardAggregate cardAggregate;
    private CardValidator cardValidator;
    private UUIDGenerator uuidGenerator;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
        cardCommandRepository = new CardCommandRepository(namedParameterJdbcTemplate);
        cardQueryRepository = new CardQueryRepository(namedParameterJdbcTemplate);
        cardValidator = new CardValidator();
        uuidGenerator = new UUIDGenerator();
        cardAggregate = new CardAggregate(cardCommandRepository, cardQueryRepository, cardValidator, uuidGenerator);
    }

    @Test
    public void createCard() {
        final Card newCard = Card.builder()
                .id(UUID.fromString("4b182412-0d6d-4857-843a-edfc1973d323"))
                .question("Was bedeutet CISC")
                .answer("Complex Instruction Set Computer")
                .tag("Wichtig")
                //.categories(categories)
                .build();
        cardCommandRepository.create(newCard);
        Card actual = cardQueryRepository.get(newCard.getId());
        Assertions.assertEquals(newCard, actual);
    }

    @Test
    public void failCreateCardWithoutId() {
        final Card newCard = Card.builder()
                .id(null)
                .question("Was bedeutet CISC")
                .answer("Complex Instruction Set Computer")
                .tag("Wichtig")
                //.categories(categories)
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> cardCommandRepository.create(newCard));

//        Card actual = cardQueryRepository.get(newCard.getId());
//        Assertions.assertEquals(newCard, actual);
    }

    @Test
    public void saveCardEmptyQuestion() {
        List<UUID> categories = Arrays.asList(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"));
        CreateCard command = CreateCard.builder()
                .question("")
                .answer("Answer")
                .tag("tag")
                .categories(categories)
                .build();

        ErrorEntry labelTakenError = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("question")
                .build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> cardAggregate.handle(command));
        Assertions.assertTrue(exception.getErrors().contains(labelTakenError));

        CreateCard command1 = CreateCard.builder()
                .question(null)
                .answer("Answer")
                .tag("tag")
                .categories(categories)
                .build();

        ErrorEntry labelTakenError1 = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("question")
                .build();
        CommandValidationException exception1 = Assertions.assertThrows(CommandValidationException.class, () -> cardAggregate.handle(command1));
        Assertions.assertTrue(exception1.getErrors().contains(labelTakenError));
    }

    @Test
    public void saveCardEmptyAnswer() {
        List<UUID> categories = Arrays.asList(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"));
        CreateCard command = CreateCard.builder()
                .question("Question")
                .answer("")
                .tag("tag")
                .categories(categories)
                .build();
        ErrorEntry labelTakenError = ErrorEntry.builder()
                .code(NOT_BLANK_VIOLATION)
                .field("answer")
                .build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> cardAggregate.handle(command));
        Assertions.assertTrue(exception.getErrors().contains(labelTakenError));

        CreateCard command1 = CreateCard.builder()
                .question("Question")
                .answer(null)
                .tag("tag")
                .categories(categories)
                .build();

        ErrorEntry labelTakenError1 = ErrorEntry.builder()
                .code(NOT_NULL_VIOLATION)
                .field("answer")
                .build();
        CommandValidationException exception1 = Assertions.assertThrows(CommandValidationException.class, () -> cardAggregate.handle(command1));
        Assertions.assertTrue(exception1.getErrors().contains(labelTakenError));
    }

    @Test
    public void saveCardNoCategory() {
        CreateCard command = CreateCard.builder()
                .question("Question")
                .answer("Answer")
                .tag("tag")
                .categories(Collections.emptyList())
                .build();
        ErrorEntry labelTakenError = ErrorEntry.builder()
                .code(CONSTRAINT_VIOLATION)
                .field("categories")
                .build();

        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class, () -> cardAggregate.handle(command));
        Assertions.assertTrue(exception.getErrors().contains(labelTakenError));

        CreateCard command1 = CreateCard.builder()
                .question("Question")
                .answer("Answer")
                .tag("tag")
                .categories(null)
                .build();
        CommandValidationException exception1 = Assertions.assertThrows(CommandValidationException.class, () -> cardAggregate.handle(command1));
        Assertions.assertTrue(exception1.getErrors().contains(labelTakenError));
    }

    @Test
    public void saveCardEmptyTag() {
        Category cat = Category.builder()
                .id(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"))
                .label("Technische Informatik")
                .build();

        final Card newCard = Card.builder()
                .id(UUID.fromString("34182412-0d6d-4857-843a-edfc1973d323"))
                .question("Was bedeutet RISC?")
                .answer("Reduced Instruction Set Computer")
                .tag("")
                .categories(Collections.singletonList(cat.getId()))
                .build();
        cardCommandRepository.create(newCard);
        Card actual = cardQueryRepository.get(newCard.getId());
        Assertions.assertEquals(newCard, actual);
        Assertions.assertEquals("", actual.getTag());
    }

    @Test
    public void deleteCard() {
        Category cat = Category.builder()
                .id(UUID.fromString("3b182412-0d6d-4857-843a-edfc1973d323"))
                .label("Algorithmentheorie")
                .build();

        final Card toDelete = Card.builder()
                .id(UUID.fromString("7b182412-0d6d-4857-843a-edfc1973d323"))
                .question("Was ist die Laufzeit von Bubble Sort?")
                .answer("O(n^2)")
                .tag("Wichtig")
                .categories(Arrays.asList(cat.getId()))
                .build();

        // first: successfull insert
        cardCommandRepository.create(toDelete);
        Assertions.assertEquals(toDelete, cardQueryRepository.get(toDelete.getId()));

        // second: successful delete
        cardCommandRepository.delete(cardQueryRepository.get(toDelete.getId()));
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> cardQueryRepository.get(toDelete.getId()));
    }

    @Test
    public void deleteNonExistentCard() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            cardQueryRepository.get(UUID.randomUUID());
        });
    }

    @Test
    public void updateCard() {
        Category cat = Category.builder()
                .id(UUID.fromString("b02cfe58-0b61-4c92-8596-c813c495cc09"))
                .label("Praktische Informatik 2")
                .build();

        final Card toBeUpdated = Card.builder()
                .id(UUID.fromString("2e7bb4ff-50fe-48db-9337-d3e8838a7df5"))
                .question("Wer hat Rot-Schwarz-Bäume erfunden?")
                .answer("Rudolf The Red Nosed Reindeer")
                .tag("Wichtig")
                .categories(Arrays.asList(cat.getId()))
                .build();

        // first: successfull insert
        cardCommandRepository.create(toBeUpdated);
        Assertions.assertEquals(toBeUpdated, cardQueryRepository.get(toBeUpdated.getId()));

        // TODO: Partial updates? War das eine bewusste Entscheidung?
        Card updated = Card.builder()
                .id(toBeUpdated.getId())
                .answer("Rudolf Bayer")
                .build();
        cardCommandRepository.update(updated);
        Assertions.assertEquals(updated, cardQueryRepository.get(toBeUpdated.getId()));
    }

    @Test
    public void updateNonExistentCard() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> cardQueryRepository.get(UUID.randomUUID()));
    }
}