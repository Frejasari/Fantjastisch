package de.fantjastisch.cards_backend.learningsystem.query;

import de.fantjastisch.cards_backend.learningsystem.repository.LearningSystemQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"file:src/main/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class LearningSystemQueryRepositoryTest {
    private LearningSystemQueryRepository learningSystemQueryRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
        learningSystemQueryRepository = new LearningSystemQueryRepository(namedParameterJdbcTemplate);
    }

    @Test
    public void shouldFindLearningSystem() {
        // TODO: Implementieren
    }

    @Test
    public void shouldNotFindLearningSystem() {
        // TODO: Implementieren
    }

    @Test
    public void shouldFindAllLearningSystems() {
        // TODO: Implementieren
    }
}
