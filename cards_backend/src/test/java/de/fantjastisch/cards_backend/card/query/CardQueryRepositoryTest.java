package de.fantjastisch.cards_backend.card.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"file:src/test/resources/schema.sql", "file:src/test/resources/test-data.sql"})
public class CardQueryRepositoryTest {
    //    private CardQueryRepository cardQueryRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    public void setUp() {
//        cardQueryRepository = new CardQueryRepository(namedParameterJdbcTemplate);
    }

    @Test
    public void test() {

    }
}

