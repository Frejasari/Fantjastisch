package de.fantjastisch.cards_backend.learningsystem;

import de.fantjastisch.cards_backend.learningsystem.aggregate.CreateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.LearningSystemAggregate;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemCommandRepository;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import de.fantjastisch.cards_backend.learningsystem.validator.LearningSystemValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Collections;

/**
 * Test Klasse für die Category Repositories
 *
 * @author Freja Sender
 */

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class LearningSystemAggregateTest {
    private LearningSystemAggregate learningSystemAggregate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
        LearningSystemCommandRepository learningSystemCommandRepository = new LearningSystemCommandRepository(namedParameterJdbcTemplate);
        LearningSystemQueryRepository learningSystemQueryRepository = new LearningSystemQueryRepository(namedParameterJdbcTemplate);
        LearningSystemValidator learningSystemValidator = new LearningSystemValidator(learningSystemQueryRepository);
        learningSystemAggregate = new LearningSystemAggregate(learningSystemCommandRepository, learningSystemValidator, learningSystemQueryRepository, new UUIDGenerator());
    }

    @Test
    public void shouldThrowWhenEmptyLabel() {
        CreateLearningSystem toCreate = CreateLearningSystem.builder()
                .label("")
                .boxLabels(Arrays.asList("Box1","Box2"))
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class,
                () -> learningSystemAggregate.handle(toCreate));

    }

    @Test
    public void shouldThrowWhenEmptyBoxLabels() {
        CreateLearningSystem toCreate = CreateLearningSystem.builder()
                .label("2Box")
                .boxLabels(Arrays.asList(""))
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class,
                () -> learningSystemAggregate.handle(toCreate));
    }


    @Test
    public void shouldThrowWhenUpdating() {
    }

    @Test
    public void shouldThrowWhenDeleting() {
    }


    // TODO: Andere Validierungen prüfen?
}
