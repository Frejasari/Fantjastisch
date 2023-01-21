package de.fantjastisch.cards_backend.learningsystem;

import de.fantjastisch.cards_backend.learningsystem.aggregate.CreateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.DeleteLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.aggregate.LearningSystemAggregate;
import de.fantjastisch.cards_backend.learningsystem.aggregate.UpdateLearningSystem;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemCommandRepository;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import de.fantjastisch.cards_backend.learningsystem.validator.LearningSystemValidator;
import de.fantjastisch.cards_backend.util.UUIDGenerator;
import de.fantjastisch.cards_backend.util.validation.CommandValidationException;
import de.fantjastisch.cards_backend.util.validation.errors.ErrorEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;


/**
 * Test Klasse für die Category Repositories
 *
 * @author Freja Sender, Alexander Kück
 */
@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class LearningSystemAggregateTest {
    private LearningSystemAggregate learningSystemAggregate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    LearningSystem learningSystem = LearningSystem
            .builder()
            .id(UUID.fromString("2be2989b-215f-49ca-ae95-366b2a3db03d"))
            .label("2Box")
            .boxLabels(Arrays.asList("Box1","Box2"))
            .build();

    @BeforeEach
    public void setUp() {
        LearningSystemCommandRepository learningSystemCommandRepository = new LearningSystemCommandRepository(namedParameterJdbcTemplate);
        LearningSystemQueryRepository learningSystemQueryRepository = new LearningSystemQueryRepository(namedParameterJdbcTemplate);
        LearningSystemValidator learningSystemValidator = new LearningSystemValidator(learningSystemQueryRepository);
        learningSystemAggregate = new LearningSystemAggregate(learningSystemCommandRepository, learningSystemValidator, learningSystemQueryRepository, new UUIDGenerator());
    }

    @Test
    public void shouldThrowWhenCreatingWithEmptyLabel() {
        CreateLearningSystem toCreate = CreateLearningSystem.builder()
                .label("")
                .boxLabels(Arrays.asList("Box1", "Box2"))
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class,
                () -> learningSystemAggregate.handle(toCreate));
    }
    @Test
    public void shouldThrowWhenUpdatingWithEmptyLabel() {

        UpdateLearningSystem toUpdate = UpdateLearningSystem.builder()
                .id(UUID.fromString("2be2989b-215f-49ca-ae95-366b2a3db03d"))
                .label("")
                .boxLabels(Arrays.asList("Box1","Box2"))
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class,
                () -> learningSystemAggregate.handle(toUpdate));
    }

    @Test
    public void shouldThrowWhenCreatingWithEmptyBoxLabels() {
        CreateLearningSystem toCreate = CreateLearningSystem.builder()
                .label("2Box")
                .boxLabels(Arrays.asList(""))
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class,
                () -> learningSystemAggregate.handle(toCreate));
    }

    @Test
    public void shouldThrowWhenUpdatingWithEmptyBoxLabels() {
        UpdateLearningSystem toUpdate = UpdateLearningSystem.builder()
                .id(UUID.fromString("2be2989b-215f-49ca-ae95-366b2a3db03d"))
                .label("2Box")
                .boxLabels(Arrays.asList(""))
                .build();
        CommandValidationException exception = Assertions.assertThrows(CommandValidationException.class,
                () -> learningSystemAggregate.handle(toUpdate));
    }
    @Test
    public void shouldThrowWhenUpdatingNotExisting() {
        UpdateLearningSystem toUpdate = UpdateLearningSystem.builder()
                .id(UUID.fromString("48039f58-ad1e-434e-a2e4-b8179ffa1c68"))
                .label("2Box")
                .boxLabels(Arrays.asList("Box1","Box2"))
                .build();
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> learningSystemAggregate.handle(toUpdate));
    }

    @Test
    public void shouldThrowWhenDeleting() {
        DeleteLearningSystem toDelete = DeleteLearningSystem
                .builder()
                .id(UUID.fromString("f5d15dd3-4670-45dc-a2bc-eed985c3a8be"))
                .build();
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> learningSystemAggregate.handle(toDelete));


    }


    // TODO: Andere Validierungen prüfen?
}
