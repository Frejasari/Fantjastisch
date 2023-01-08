package de.fantjastisch.cards_backend.learningsystem.command;

import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class LearningSystemCommandRepositoryTest {
    private LearningSystemCommandRepository learningSystemCommandRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
        learningSystemCommandRepository = new LearningSystemCommandRepository(namedParameterJdbcTemplate);
    }

    @Test
    public void shouldSaveLearningSystem() {
    }

    @Test
    public void shouldUpdateLearningSystemLabel() {
    }

    @Test
    public void shouldDeleteLearningSystem() {
    }

    // TODO: Welche Fehler kann namedParameterJdbcTemplate werfen?
}
