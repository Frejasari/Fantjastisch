package de.fantjastisch.cards_backend.learningsystem;

import de.fantjastisch.cards_backend.learningsystem.aggregate.LearningSystemAggregate;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemCommandRepository;
import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import de.fantjastisch.cards_backend.learningsystem.validator.LearningSystemValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

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
        learningSystemAggregate = new LearningSystemAggregate(learningSystemCommandRepository, learningSystemValidator, learningSystemQueryRepository);
    }

    @Test
    public void shouldThrowWhenSavingWithNameTaken() {
    }

    @Test
    public void shouldThrowWhenUpdating() {
    }

    @Test
    public void shouldThrowWhenDeleting() {
    }

    // TODO: Andere Validierungen prüfen?
}
